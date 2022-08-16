package com.java.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;

import com.java.dto.EmailDTO;
import com.java.entity.CartItem;
import com.java.entity.Customer;
import com.java.entity.Order;
import com.java.entity.OrderDetail;
import com.java.entity.Product;
import com.java.repository.CustomersRepository;
import com.java.repository.OrderDetailRepository;
import com.java.repository.OrderRepository;
import com.java.repository.ProductRepository;
import com.java.service.ShoppingCartService;
import com.java.service.WishListService;
import com.java.service.impl.EmailServiceImpl;
import com.java.utils.Constants;

@Controller
public class ShoppingCartController extends CommonController {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	CustomersRepository customersRepository;

	@Autowired
	ShoppingCartService shoppingCartService;
	
	@Autowired
	WishListService wishListService;

	@Autowired
	HttpSession session;

	public ShoppingCartController(ProductRepository productRepository, OrderRepository orderRepository,
			OrderDetailRepository orderDetailRepository, ShoppingCartService shoppingCartService,
			CustomersRepository customersRepository, WishListService wishListService) {
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
		this.orderDetailRepository = orderDetailRepository;
		this.shoppingCartService = shoppingCartService;
		this.customersRepository = customersRepository;
		this.wishListService = wishListService;
	}

	@GetMapping(value = "/carts")
	public String shoppingCart(Model model) {
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", shoppingCartService.getAmount());
		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
			totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
		}

		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalCartItemWishs", wishListService.getCount());
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		
		return "site/shoppingCart";
	}

	@GetMapping(value = "/addToCart")
	public String add(@RequestParam("productId") Integer productId, HttpServletRequest request, Model model) {

		Product product = productRepository.findById(productId).orElse(null);

		session = request.getSession();
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		if (product != null) {
			CartItem item = new CartItem();
			BeanUtils.copyProperties(product, item);
			item.setQuantity(1);
			item.setProduct(product);
			item.setProductId(productId);
			shoppingCartService.add(item);
		}
		session.setAttribute("cartItems", cartItems);
		model.addAttribute("totalCartItemWishs", wishListService.getCount());
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		
		return "redirect:/carts";
	}
	@RequestMapping("cart/update/{id}")
	public String updateCart(@PathVariable("id") Optional<Integer> id,
			@RequestParam("quantity") Optional<Integer> quantity, Model m) {
		if (id.isPresent() && quantity.isPresent()) {
			shoppingCartService.update(id.get(), quantity.get());
		}
		return "redirect:/carts";
	}

	@SuppressWarnings("unlikely-arg-type")
	@GetMapping(value = "/remove/{id}")
	public String remove(@PathVariable("id") Integer id, HttpServletRequest request, Model model) {
		Product product = productRepository.findById(id).orElse(null);

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		session = request.getSession();
		if (product != null) {
			CartItem item = new CartItem();
			BeanUtils.copyProperties(product, item);
			item.setProduct(product);
			cartItems.remove(session);
			shoppingCartService.remove(item);
		}
		model.addAttribute("totalCartItemWishs", wishListService.getCount());
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		
		return "redirect:/carts";
	}

	@GetMapping(value = "/checkout")
	public String checkOut(Model model) {

		Order order = new Order();
		model.addAttribute("order", order);

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", shoppingCartService.getAmount());
		model.addAttribute("NoOfItems", shoppingCartService.getCount());
		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
			totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
		}

		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("totalCartItemWishs", wishListService.getCount());
		model.addAttribute("totalCartItems", shoppingCartService.getCount());

		return "site/checkOut";
	}

	@PostMapping(value = "/checkout")
	@Transactional
	public String checkedOut(Model model, Order order, HttpServletRequest request, Principal principal) {
		
		InternetAddress[] internetAddress = new InternetAddress[1];
        EmailDTO emailDTO = new EmailDTO();
        EmailServiceImpl emailServiceImpl = new EmailServiceImpl();
        Context context = new Context();
        Order newOrder;
		
		session = request.getSession();
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		
		Customer c = customersRepository.FindByEmail(principal.getName()).get();

		double totalPrice = 0;

		for (CartItem cartItem : cartItems) {

			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setQuantity(cartItem.getQuantity());
			orderDetail.setOrder(order);
			orderDetail.setProduct(cartItem.getProduct());
			orderDetail.setDiscount(cartItem.getProduct().getDiscount());

			double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
			totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);

			double unitPrice = cartItem.getProduct().getPrice();

			orderDetail.setTotalPrice(price - (price * cartItem.getProduct().getDiscount() / 100));
			orderDetail.setPrice(unitPrice);
			orderDetail.setStatus("Đang Chờ Xử Lý");
			orderDetailRepository.save(orderDetail);

		}

		order.setTotalPrice(totalPrice);
		Date date = new Date();
		order.setOrderDate(date);
		order.setAmount(shoppingCartService.getAmount());
		order.setCustomer(c);
		order.setStatus(0);

		newOrder = orderRepository.save(order);
		
		try {
			
			List<OrderDetail> orderDetails = orderDetailRepository.findOrderDetailsByOrderID(newOrder);
			
            internetAddress[0] = new InternetAddress(c.getEmail());
            context.setVariable("order", newOrder);
            context.setVariable("orderId", newOrder.getOrderId());
            context.setVariable("orderDetails", orderDetails);
            emailDTO.setContext(context);
            emailDTO.setEmailTemplate(Constants.ORDER_EMAIL_TEMPLATE);
            emailDTO.setSubject("New Order");
            emailDTO.setInternetAddresses(internetAddress);
            emailServiceImpl.setEmailDTO(emailDTO);
            emailServiceImpl.start();
            
        } catch (AddressException e) {
            System.err.println("Can not send gmai!");
        }		

		shoppingCartService.clear();
		wishListService.clear();
		session.removeAttribute("cartItems");
		model.addAttribute("orderId", order.getOrderId());
		model.addAttribute("totalCartItemWishs", wishListService.getCount());
		model.addAttribute("totalCartItems", shoppingCartService.getCount());
		
		return "site/checkout_success";
	}
	
	@GetMapping("create")
    public String createPage(Model model) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String timeFormatted = currentTime.format(formatter);

        Order order = new Order();
        model.addAttribute("currentTime", timeFormatted);
        model.addAttribute("order", order);
        return "create_order";
    }

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}

}

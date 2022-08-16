package com.java.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


import com.java.entity.CartItem;
import com.java.entity.Customer;
import com.java.entity.Order;
import com.java.entity.OrderDetail;
import com.java.entity.Product;
import com.java.repository.CustomersRepository;
import com.java.repository.OrderDetailRepository;
import com.java.repository.OrderRepository;
import com.java.repository.ProductRepository;
import com.java.service.EmailService;
import com.java.service.ShoppingCartService;
import com.java.service.WishListService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Controller
public class ShoppingCartController extends CommonController {



//    @Value("${host.url}")
//    private String hostUrl;
    
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

//	@Autowired
//	SendMailService sendMailService;

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
//		this.sendMailService = sendMailService;
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

	// add cartItem
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

	// delete cartItem
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

	// show check out
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

	// submit checkout
	@PostMapping(value = "/checkout")
	@Transactional
	public String checkedOut(Model model, Order order, HttpServletRequest request, Principal principal) {

		session = request.getSession();
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		
		Customer c = customersRepository.FindByEmail(principal.getName()).get();

		double totalPrice = 0;

		for (CartItem cartItem : cartItems) {

			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setQuantity(cartItem.getQuantity());
			orderDetail.setOrder(order);
			orderDetail.setProduct(cartItem.getProduct());

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

		orderRepository.save(order);
		order.getOrderId();
		
		String htmlString = "";
		
		for (CartItem cartItem : cartItems) {
			htmlString = htmlString+" <h3>" + cartItem.getName() +":" + cartItem.getProduct().getPrice()+"</h3>";
			
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


//    @PostMapping("create")
//    public String createOrder(HttpServletRequest request) throws IOException {
////        Create new Order with pending status
//        Long orderAmount = Long.parseLong(request.getParameter("amount")) * 100;
//        String orderDes = request.getParameter("description");
////        Order newOrder = new Order(orderAmount, orderDes);
//        String ipAddr = request.getRemoteAddr();
////        log.info("ip: " + ipAddr);
////        Long createdOrderId = orderService.createOrder(newOrder, ipAddr);
//
////        Query Parameter
//        String vnp_Version = "2.0.1";
//
//        String vnp_Command = "pay";
//
//        String vnp_TmnCode = Config.vnp_TmnCode;
//
//        String vnp_Amount = orderAmount.toString();
//
//        String vnp_BankCode = request.getParameter("bankcode");
//
//        LocalDateTime time = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//        String vnp_CreateDate = time.format(formatter);
//
//        String vnp_CurrCode = "VND";
//
//        String vnp_IpAddr = ipAddr;
//
//        String vnp_Locale = request.getParameter("language");
//
//        String vnp_OrderInfo = orderDes;
//
//        String vnp_OrderType = request.getParameter("ordertype");
//
////        String vnp_ReturnUrl = Config.vnp_Returnurl;
//        String vnp_ReturnUrl = hostUrl + "return";
//
////        String vnp_TxnRef = createdOrderId.toString();
////        String vnp_TxnRef = Helper.getRandomNumber(8);
//
//        Map<String, String> params = new HashMap<>();
//
//        params.put("vnp_Version", vnp_Version);
//        params.put("vnp_Command", vnp_Command);
//        params.put("vnp_TmnCode", vnp_TmnCode);
//        params.put("vnp_Amount", vnp_Amount);
//        params.put("vnp_BankCode", vnp_BankCode);
//        params.put("vnp_CreateDate", vnp_CreateDate);
//        params.put("vnp_CurrCode", vnp_CurrCode);
//        params.put("vnp_IpAddr", vnp_IpAddr);
//        params.put("vnp_Locale", vnp_Locale);
//        params.put("vnp_OrderInfo", vnp_OrderInfo);
//        params.put("vnp_OrderType", vnp_OrderType);
//        params.put("vnp_ReturnUrl", vnp_ReturnUrl);
////        params.put("vnp_TxnRef", vnp_TxnRef);
//
//        List fieldNames = new ArrayList(params.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//        Iterator itr = fieldNames.iterator();
//        while (itr.hasNext()) {
//            String fieldName = (String) itr.next();
//            String fieldValue = params.get(fieldName);
//            if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                //Build hash data
//                hashData.append(fieldName);
//                hashData.append('=');
//                hashData.append(fieldValue);
//                //Build query
//                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
//                query.append('=');
//                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                if (itr.hasNext()) {
//                    query.append('&');
//                    hashData.append('&');
//                }
//            }
//        }
//        String queryUrl = query.toString();
//        String vnp_SecureHash = Helper.Sha256(Config.vnp_HashSecret + hashData);
//  
//        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//
//        String paymentUrl = Config.vnp_PayUrl + '?' + queryUrl;
//
//        return "redirect:" + paymentUrl;
//    }


	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}

}

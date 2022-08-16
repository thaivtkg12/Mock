package com.java.controller.admin;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.java.config.UploadImage;
import com.java.controller.CommonController;
import com.java.entity.Category;
import com.java.entity.Product;
import com.java.entity.ProductExporter;
import com.java.entity.Supplier;
import com.java.service.CategoryService;
import com.java.service.ProductService;
import com.java.service.SupplierService;

@Controller
public class ProductController extends CommonController {

	@Value("${upload.path}")
	private String pathUploadImage;

	@Autowired
	ProductService productService;

	@Autowired
	UploadImage saveImage;

	@Autowired
	CategoryService categoryService;

	@Autowired
	SupplierService supplierService;

	public ProductController(CategoryService categoryService, SupplierService supplierService,
			ProductService productService) {
		this.productService = productService;
		this.categoryService = categoryService;
		this.supplierService = supplierService;
	}

	// show list product - table list
	@ModelAttribute("products")
	public List<Product> showProduct(Model model) {
		List<Product> products = productService.findAll();

		model.addAttribute("products", products);

		return products;
	}

	@GetMapping(value = "/admin/products")
	public String products(Model model, @ModelAttribute("product") Product product, Principal principal) {

		return "admin/products";
	}

	// thêm sản phầm
	@PostMapping(value = "/addProduct")
	public String addProduct(Product product, RedirectAttributes model, @RequestParam("file") MultipartFile file,
			HttpServletRequest httpServletRequest) {
		if (file.isEmpty()) {
			product.setImage("default-image.jpg");
		} else {
			saveImage.saveImage(file);
			product.setImage(file.getOriginalFilename());
		}

		if (categoryService.findByCategoryId(product.getCategory().getCategoryId()) != null) {
			if (supplierService.findById(product.getSupplier().getId()).isPresent()) {
				Product p = productService.save(product);
				if (p != null) {
					model.addAttribute("message", "Add success");
				} else {
					model.addAttribute("error", "Add failure");
				}
			}else {
				model.addAttribute("error","Suppler not exist");
			}
		} else {
			model.addAttribute("error", "Category not exist");
		}

		return "redirect:/admin/products";
	}

	@PostMapping("/updateProduct/{id}")
	public String processEditProduct(@PathVariable("id") Integer id, ModelMap model, Product product,
			@RequestParam("file") MultipartFile file) {
		Product saveProduct = productService.findByIdProduct(id);
		if (file.isEmpty()) {
			product.setImage(saveProduct.getImage());
		} else {
			saveImage.saveImage(file);
			product.setImage(file.getOriginalFilename());
		}
		productService.save(product);
		return "redirect:/admin/products";
	}

	// show select option ở add product
	@ModelAttribute("categoryList")
	public List<Category> showCategory(Model model) {
		List<Category> categoryList = categoryService.findAll();
		model.addAttribute("categoryList", categoryList);

		return categoryList;
	}

	// show select option ở add product
	@ModelAttribute("supplierList")
	public List<Supplier> supplierList(Model model) {
		List<Supplier> supplierList = supplierService.findAll();
		model.addAttribute("supplierList", supplierList);

		return supplierList;
	}

	// get Edit product
	@GetMapping(value = "/editProduct/{id}")
	public String editProduct(@PathVariable("id") Integer id, ModelMap model) {
		Product product = productService.findById(id).orElse(null);
		model.addAttribute("product", product);
		return "admin/editProduct";
	}

	// delete product
	@GetMapping("/deleteProduct/{id}")
	public String delProduct(@PathVariable("id") Integer id, Model model) {
		productService.deleteById(id);
		model.addAttribute("message", "Delete successful!");

		return "redirect:/admin/products";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}

	@GetMapping(value = "/exportproduct")
	public void exportToExcel(HttpServletResponse response) throws IOException {

		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachement; filename=product.xlsx";

		response.setHeader(headerKey, headerValue);

		List<Product> lisProducts = productService.findAll();

		ProductExporter excelExporter = new ProductExporter(lisProducts);
		excelExporter.export(response);

	}

	private static String UPLOADED_FOLDER = "/Users/macbook/Downloads//";

	@PostMapping("/upload") // //new annotation since 4.3
	public String singleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);

		} catch (IOException e) {
			e.printStackTrace();
		}

		model.addAttribute(file.getOriginalFilename(), "filename1");
		return "redirect:/admin/products";
	}

	@GetMapping("/excel")
	public String Exceld(@RequestParam("filename") String filename, int index) {

		String jdbcURL = "jdbc:mysql://localhost:3306/mock";
		String username = "root";
		String password = "khanhtuong09";
		String excelFilePath = "/Users/macbook/Downloads/" + filename;

		int batchSize = index;

		Connection connection = null;

		try {
			long start = System.currentTimeMillis();

			FileInputStream inputStream = new FileInputStream(excelFilePath);

			Workbook workbook = new XSSFWorkbook(inputStream);

			Sheet firstSheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = firstSheet.iterator();

			connection = DriverManager.getConnection(jdbcURL, username, password);
			connection.setAutoCommit(false);

			String sql = "INSERT INTO students (name, progress) VALUES (?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			int count = 0;

			rowIterator.next(); // skip the header row

			while (rowIterator.hasNext()) {
				Row nextRow = rowIterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();

				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();

					int columnIndex = nextCell.getColumnIndex();

					switch (columnIndex) {
					case 0:
						String name = nextCell.getStringCellValue();
						statement.setString(1, name);
						break;
//					case 1:
//						Date enrollDate = nextCell.getDateCellValue();
//						statement.setTimestamp(2, new Timestamp(enrollDate.getTime()));
					case 1:
						int progress = (int) nextCell.getNumericCellValue();
						statement.setInt(2, progress);
					}

				}

				statement.addBatch();

				if (count % batchSize == 0) {
					statement.executeBatch();
				}

			}

			workbook.close();

			// execute the remaining queries
			statement.executeBatch();

			connection.commit();
			connection.close();

			long end = System.currentTimeMillis();
			System.out.printf("Import done in %d ms\n", (end - start));

		} catch (IOException ex1) {
			System.out.println("Error reading file");
			ex1.printStackTrace();
		} catch (SQLException ex2) {
			System.out.println("Database error");
			ex2.printStackTrace();
		}
		return "redirect:/admin/products";

	}

}

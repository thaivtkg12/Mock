package com.java.entity;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.Data;

@Data
public class ProductExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	private List<Product> listProducts;

	public ProductExporter(List<Product> listProducts) {

		this.listProducts = listProducts;
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("OrderDetails");
	}

	private void writeHeaderRow() {

		Row row = sheet.createRow(0);

		Cell cell = row.createCell(0);
		cell.setCellValue("Name");

		cell = row.createCell(1);
		cell.setCellValue("Product Description");

		cell = row.createCell(2);
		cell.setCellValue("Price");

		cell = row.createCell(3);
		cell.setCellValue("Quantity");

	}

	private void writeDataRows() {
		int rowCount = 1;
		for (Product product : listProducts) {
			Row row = sheet.createRow(rowCount++);

			Cell cell = row.createCell(0);
			cell.setCellValue(product.getName());

			cell = row.createCell(1);
			cell.setCellValue(product.getDescription());

			cell = row.createCell(2);
			cell.setCellValue(product.getPrice());

			cell = row.createCell(3);
			cell.setCellValue(product.getQuantity());

		}

	}

	public void export(HttpServletResponse response) throws IOException {

		writeHeaderRow();
		writeDataRows();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();

	}

}

package com.training.socialnetwork.util.constant.generator;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.training.socialnetwork.dto.response.user.UserReportDto;

public class ReportGenerator {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private UserReportDto user;
	
	public ReportGenerator(UserReportDto user) {
		this.user = user;
		this.workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Report");
		
		Row row = sheet.createRow(0);
		
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(15);
		style.setFont(font);
		createCell(row, 0, "Number of posts", style);
		createCell(row, 1, "Number of comments", style);
		createCell(row, 2, "Number of friends", style);
		createCell(row, 3, "Number of likes", style);
	}
	
	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}
	
	private void writeDataLines() {
		int rowCount = 1;
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(13);
		style.setFont(font);
		
		Row row = sheet.createRow(rowCount);
		int columnCount = 0;
		
		createCell(row, columnCount++, user.getPostCount(), style);
		createCell(row, columnCount++, user.getCommentCount(), style);
		createCell(row, columnCount++, user.getFriendCount(), style);
		createCell(row, columnCount++, user.getLikeCount(), style);
	}
	
	public void export(HttpServletResponse response) throws IOException {
		writeHeaderLine();
		writeDataLines();
		
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		
		outputStream.close();
	}
}

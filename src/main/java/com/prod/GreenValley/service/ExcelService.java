package com.prod.GreenValley.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.prod.GreenValley.DTO.ProductStockDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


public class ExcelService {


     public static byte[] generateProductStockExcel(List<ProductStockDTO> products) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Product Stock");

        // Create header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Name", "Purchase Quantity", "Sale Quantity", "Price per Unit"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Fill data
        int rowIndex = 1;
        for (ProductStockDTO product : products) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(product.getId());
            row.createCell(1).setCellValue(product.getName());
            row.createCell(2).setCellValue(product.getPurchaseQuantity());
            row.createCell(3).setCellValue(product.getSaleQuantity());
            row.createCell(4).setCellValue(product.getPricePerUnit() != null ? product.getPricePerUnit().doubleValue() : 0);
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return bos.toByteArray();
    }
    
}

package com.example.investi.Services;

import com.example.investi.Entities.Training;
import com.example.investi.Repositories.TrainingRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    @Autowired
    private TrainingRepository trainingRepository;

    public void generateFormationHistoryExcel(HttpServletResponse response) throws IOException {
        // Get all trainings from the database
        List<Training> trainings = trainingRepository.findAll();

        // Create an Excel workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Trainings History");

        // Create a CellStyle for header with background color
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Create the first row (headers)
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Category");
        headerRow.createCell(2).setCellValue("Description");
        headerRow.createCell(3).setCellValue("Duration");
        headerRow.createCell(4).setCellValue("Level");
        headerRow.createCell(5).setCellValue("Title");
        headerRow.createCell(6).setCellValue("Trainer_ID");
        headerRow.createCell(7).setCellValue("Status");

        // Apply header style to the header cells
        for (int i = 0; i < 8; i++) {
            headerRow.getCell(i).setCellStyle(headerStyle);
        }

        // Create a CellStyle for data rows
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setWrapText(true);  // Allow text wrapping in cells

        // Add data to the Excel sheet
        int rowNum = 1;
        for (Training training : trainings) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(training.getId());

            // Check if the category is null, then add it or a default value
            row.createCell(1).setCellValue(training.getCategory() != null ? training.getCategory().toString() : "N/A");

            // Similarly handle other fields
            row.createCell(2).setCellValue(training.getDescription() != null ? training.getDescription() : "N/A");
            row.createCell(3).setCellValue(training.getDuration());
            row.createCell(4).setCellValue(training.getLevel() != null ? training.getLevel().toString() : "N/A");
            row.createCell(5).setCellValue(training.getTitle() != null ? training.getTitle() : "N/A");
            row.createCell(6).setCellValue(training.getTrainer() != null ? training.getTrainer().toString() : "N/A");
            row.createCell(7).setCellValue(training.getStatus() != null ? training.getStatus().toString() : "N/A");

            // Apply data row style
            for (int i = 0; i < 8; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }

            // Optional: Alternate row color for better readability
            if (rowNum % 2 == 0) {
                CellStyle alternateRowStyle = workbook.createCellStyle();
                alternateRowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                alternateRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                // Apply alternate row color
                for (int i = 0; i < 8; i++) {
                    row.getCell(i).setCellStyle(alternateRowStyle);
                }
            }
        }

        // Adjust column widths to fit content (with some padding)
        for (int i = 0; i < 8; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 512); // Add extra space for padding
        }



        // Write the Excel file to the HTTP response
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=historique_formations.xlsx");

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            outputStream.writeTo(response.getOutputStream());
        }

        // Close the workbook
        workbook.close();
    }
}
package com.acqu.co.excel.converter.actuator.util;

import com.acqu.co.excel.converter.actuator.exception.ExcelImportException;
import com.acqu.co.excel.converter.actuator.model.AcquUserEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExcelHelper {


    public List<AcquUserEntity> parseExcelFileForAcquUserEntity(MultipartFile file) throws IOException, ExcelImportException {
        List<AcquUserEntity> acquUserEntities = new ArrayList<>();
        // Create a Set to track unique userEntityIds
        Set<Long> existingIds = new HashSet<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                AcquUserEntity entity = new AcquUserEntity();
                Long userEntityId = Long.valueOf(row.getCell(0).getStringCellValue());

                // Check for duplicate userEntityId
                if (existingIds.contains(userEntityId)) {
                    throw new ExcelImportException("Duplicate ID found: " + userEntityId);
                }
                existingIds.add(userEntityId); // Add to the set for tracking

                entity.setUserEntityId(userEntityId);
                entity.setUserName(row.getCell(1).getStringCellValue());
                entity.setCreatedDate(row.getCell(2).getDateCellValue());
                entity.setUpdatedDate(row.getCell(3).getDateCellValue());
                acquUserEntities.add(entity);
            }
        }

        return acquUserEntities;
    }

    public InputStreamResource createWorkbook(List<AcquUserEntity> all) throws IOException {
        if (all == null || all.isEmpty()) {
            return null;
        }

        String sheetName = "AcquUserEntity";
        String[] headers = {"User Entity ID", "User Name", "Created Date", "Updated Date"};

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XSSFSheet sheet = workbook.createSheet(sheetName);

            XSSFFont hdrFont = workbook.createFont();
            hdrFont.setBold(true);
            hdrFont.setColor(IndexedColors.WHITE.getIndex());

            XSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(hdrFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            int rowNum = 0;
            int cellNum = 0;

            // Create header row
            Row row = sheet.createRow(rowNum++);
            for (String header : headers) {
                Cell cell = row.createCell(cellNum++);
                cell.setCellValue(header);
                cell.setCellStyle(headerStyle);
            }

            // Create data rows
            for (AcquUserEntity entity : all) {
                row = sheet.createRow(rowNum++);
                cellNum = 0;

                row.createCell(cellNum++).setCellValue(entity.getUserEntityId());
                row.createCell(cellNum++).setCellValue(entity.getUserName());
                row.createCell(cellNum++).setCellValue(entity.getCreatedDate() != null ? entity.getCreatedDate().toString() : "");
                row.createCell(cellNum++).setCellValue(entity.getUpdatedDate() != null ? entity.getUpdatedDate().toString() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        }
    }

    // After
    public List<AcquUserEntity> parseCsvFileForAcquUserEntity(MultipartFile file) {
        List<AcquUserEntity> acquUserEntities = new ArrayList<>();
        Set<Long> existingIds = new HashSet<>();

        try (var reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header row
                }

                String[] fields = line.split(",");
                AcquUserEntity entity = new AcquUserEntity();
                Long userEntityId = Long.valueOf(fields[0]);

                // Check for duplicate userEntityId
                if (existingIds.contains(userEntityId)) {
                    throw new ExcelImportException("Duplicate ID found: " + userEntityId);
                }
                existingIds.add(userEntityId); // Add to the set for tracking

                entity.setUserEntityId(userEntityId);
                entity.setUserName(fields[1]);
                entity.setCreatedDate(new SimpleDateFormat("yyyy-MM-dd").parse(fields[2]));
                entity.setUpdatedDate(new SimpleDateFormat("yyyy-MM-dd").parse(fields[3]));
                acquUserEntities.add(entity);
            }
        } catch (IOException  | ParseException e) {
            throw new ExcelImportException("Failed to parse CSV file", e);
        }

        return acquUserEntities;
    }
}

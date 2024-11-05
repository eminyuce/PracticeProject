

package com.acqu.co.excel.converter.actuator.service.impl;

import com.acqu.co.excel.converter.actuator.exception.ExcelImportException;
import com.acqu.co.excel.converter.actuator.model.AcquUserEntity;
import com.acqu.co.excel.converter.actuator.repo.AcquUserEntityRepository;
import com.acqu.co.excel.converter.actuator.service.AcquUserEntityService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AcquUserEntityServiceImpl implements AcquUserEntityService {

    private final AcquUserEntityRepository acquUserEntityRepository;

    @Autowired
    public AcquUserEntityServiceImpl(AcquUserEntityRepository acquUserEntityRepository) {
        this.acquUserEntityRepository = acquUserEntityRepository;
    }

    @Override
    public List<AcquUserEntity> findAll(Sort sort) {
        return acquUserEntityRepository.findAll(sort);
    }

    @Override
    public InputStreamResource exportXls() throws IOException {
        List<AcquUserEntity> allUsers = acquUserEntityRepository.findAll();
        return createWorkbook(allUsers);
    }

    @Override
    @Transactional
    public List<AcquUserEntity> uploadAcquUserEntityFromExcel(MultipartFile file) throws IOException, ExcelImportException {
        List<AcquUserEntity> users = parseExcelFileForAcquUserEntity(file);
        // Delete existing records by their IDs
        List<String> idsToDelete = null;
        if (!idsToDelete.isEmpty()) {
            acquUserEntityRepository.deleteByIds(idsToDelete);
        }

// Save the new records
        return acquUserEntityRepository.saveAll(users);
    }

    @Override
    public Page<AcquUserEntity> findAll(String search, Pageable pageable) {
        Specification<AcquUserEntity> spec = Specification.where(null);

        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + search + "%"));
        }

        //return acquUserEntityRepository.findAll(spec, pageable);
        return null;
    }

    private List<AcquUserEntity> parseExcelFileForAcquUserEntity(MultipartFile file) throws IOException, ExcelImportException {
        List<AcquUserEntity> acquUserEntities = new ArrayList<>();
        // Create a Set to track unique userEntityIds
        Set<String> existingIds = new HashSet<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                AcquUserEntity entity = new AcquUserEntity();
                String userEntityId = row.getCell(0).getStringCellValue();

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

    private InputStreamResource createWorkbook(List<AcquUserEntity> all) throws IOException {
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


}

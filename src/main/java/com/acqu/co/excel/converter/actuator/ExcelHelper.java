
 @Service
 public class ExcelHelper{

 
 
 // Method to create workbook for AcquUserEntity
    public InputStreamResource createAcquUserEntityWorkbook(List<AcquUserEntity> all) throws IOException {
        if (all == null || all.isEmpty()) {
            return null;
        }

        String sheetName = "AcquUserEntity";
        String[] headers = { "User Entity ID", "User Name", "Created Date", "Updated Date" };

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

    // Method to parse Excel file for AcquUserEntity
    public List<AcquUserEntity> parseExcelFileForAcquUserEntity(MultipartFile file) throws IOException {
        List<AcquUserEntity> acquUserEntities = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }
                AcquUserEntity entity = new AcquUserEntity();
                entity.setUserEntityId(row.getCell(0).getStringCellValue());
                entity.setUserName(row.getCell(1).getStringCellValue());
                entity.setCreatedDate(row.getCell(2).getDateCellValue());
                entity.setUpdatedDate(row.getCell(3).getDateCellValue());
                acquUserEntities.add(entity);
            }
        }
        return acquUserEntities;
    }

   
}
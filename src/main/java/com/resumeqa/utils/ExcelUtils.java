package com.resumeqa.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public final class ExcelUtils {
    private static final DataFormatter FORMATTER = new DataFormatter();

    private ExcelUtils() {
    }

    public static Object[][] getSheetData(String sheetName) {
        List<Map<String, String>> rows = getRows(sheetName);
        Object[][] data = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i);
        }
        return data;
    }

    public static List<Map<String, String>> getRows(String sheetName) {
        Path path = Path.of(ConfigReader.get("testDataPath"));
        try (FileInputStream input = new FileInputStream(path.toFile());
             Workbook workbook = new XSSFWorkbook(input)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet not found: " + sheetName);
            }

            Row header = sheet.getRow(0);
            if (header == null) {
                throw new IllegalArgumentException("Sheet has no header row: " + sheetName);
            }

            List<String> columns = new ArrayList<>();
            for (Cell cell : header) {
                columns.add(FORMATTER.formatCellValue(cell).trim());
            }

            List<Map<String, String>> data = new ArrayList<>();
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                Map<String, String> rowData = new LinkedHashMap<>();
                boolean hasValue = false;
                for (int colIndex = 0; colIndex < columns.size(); colIndex++) {
                    String value = FORMATTER.formatCellValue(row.getCell(colIndex)).trim();
                    rowData.put(columns.get(colIndex), value);
                    hasValue = hasValue || !value.isBlank();
                }
                if (hasValue) {
                    data.add(rowData);
                }
            }
            return data;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read test data workbook: " + path.toAbsolutePath(), e);
        }
    }

    public static Map<String, String> findById(String sheetName, String idColumn, String id) {
        return getRows(sheetName).stream()
                .filter(row -> id.equalsIgnoreCase(row.get(idColumn)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No row found in " + sheetName + " where " + idColumn + "=" + id));
    }
}

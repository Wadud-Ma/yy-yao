package com.yy.yao.service;

import com.yy.yao.entity.Annotation;
import com.yy.yao.entity.ClassicText;
import com.yy.yao.entity.Translation;
import com.yy.yao.repository.AnnotationRepository;
import com.yy.yao.repository.ClassicTextRepository;
import com.yy.yao.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel数据导入服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelImportService {

    private final ClassicTextRepository classicTextRepository;
    private final AnnotationRepository annotationRepository;
    private final TranslationRepository translationRepository;

    /**
     * 从Excel文件导入易经数据
     *
     * @param filePath Excel文件路径
     * @return 导入的记录数
     */
    @Transactional
    public int importFromExcel(String filePath) throws IOException {
        log.info("开始从Excel导入数据: {}", filePath);

        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("文件不存在: " + filePath);
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            int totalImported = 0;

            // 遍历所有工作表
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                log.info("处理工作表: {}", sheetName);

                int imported = processSheet(sheet);
                totalImported += imported;
                log.info("工作表 {} 导入了 {} 条记录", sheetName, imported);
            }

            log.info("Excel导入完成, 总共导入 {} 条记录", totalImported);
            return totalImported;
        }
    }

    /**
     * 处理单个工作表
     */
    private int processSheet(Sheet sheet) {
        int imported = 0;

        // 读取表头
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            log.warn("工作表 {} 没有表头行", sheet.getSheetName());
            return 0;
        }

        // 解析表头, 获取列索引
        ColumnMapping mapping = parseHeader(headerRow);

        // 从第二行开始读取数据
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null || isEmptyRow(row)) {
                continue;
            }

            try {
                processRow(row, mapping);
                imported++;
            } catch (Exception e) {
                log.error("处理第 {} 行时出错: {}", rowIndex + 1, e.getMessage(), e);
            }
        }

        return imported;
    }

    /**
     * 解析表头，获取列映射
     */
    private ColumnMapping parseHeader(Row headerRow) {
        ColumnMapping mapping = new ColumnMapping();

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell == null) continue;

            String header = getCellValueAsString(cell).trim();

            switch (header) {
                case "卦名", "章节", "chapter" -> mapping.chapterCol = i;
                case "标题", "title" -> mapping.titleCol = i;
                case "原文", "content" -> mapping.contentCol = i;
                case "作者", "author" -> mapping.authorCol = i;
                case "朝代", "dynasty" -> mapping.dynastyCol = i;
                case "注释", "annotation" -> mapping.annotationCol = i;
                case "标记", "marker" -> mapping.markerCol = i;
                case "注释类型", "type" -> mapping.typeCol = i;
                case "译文", "translation" -> mapping.translationCol = i;
                case "译者", "translator" -> mapping.translatorCol = i;
            }
        }

        return mapping;
    }

    /**
     * 处理单行数据
     */
    private void processRow(Row row, ColumnMapping mapping) {
        // 读取基本信息
        String chapter = getCellValue(row, mapping.chapterCol);
        String title = getCellValue(row, mapping.titleCol);
        String content = getCellValue(row, mapping.contentCol);
        String author = getCellValue(row, mapping.authorCol);
        String dynasty = getCellValue(row, mapping.dynastyCol);

        if (content == null || content.isBlank()) {
            return; // 跳过空内容
        }

        // 构建标题
        if (title == null || title.isBlank()) {
            title = chapter != null ? "《周易·" + chapter + "》" : "未命名";
        }

        // 保存原文
        ClassicText text = ClassicText.builder()
                .title(title)
                .chapter(chapter)
                .content(content)
                .author(author)
                .dynasty(dynasty)
                .build();

        text = classicTextRepository.save(text);

        // 处理注释
        String annotationContent = getCellValue(row, mapping.annotationCol);
        String marker = getCellValue(row, mapping.markerCol);
        String type = getCellValue(row, mapping.typeCol);

        if (annotationContent != null && !annotationContent.isBlank()) {
            Annotation annotation = Annotation.builder()
                    .textId(text.getId())
                    .marker(marker != null ? marker : "[1]")
                    .content(annotationContent)
                    .type(type != null ? type : "注释")
                    .build();
            annotationRepository.save(annotation);
        }

        // 处理译文
        String translationContent = getCellValue(row, mapping.translationCol);
        String translator = getCellValue(row, mapping.translatorCol);

        if (translationContent != null && !translationContent.isBlank()) {
            Translation translation = Translation.builder()
                    .textId(text.getId())
                    .content(translationContent)
                    .translator(translator)
                    .build();
            translationRepository.save(translation);
        }
    }

    /**
     * 获取单元格值
     */
    private String getCellValue(Row row, int columnIndex) {
        if (columnIndex < 0) return null;

        Cell cell = row.getCell(columnIndex);
        if (cell == null) return null;

        return getCellValueAsString(cell);
    }

    /**
     * 将单元格值转为字符串
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue().toString();
                } else {
                    yield String.valueOf((long) cell.getNumericCellValue());
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    /**
     * 检查是否为空行
     */
    private boolean isEmptyRow(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = getCellValueAsString(cell);
                if (value != null && !value.isBlank()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 列映射类
     */
    private static class ColumnMapping {
        int chapterCol = -1;
        int titleCol = -1;
        int contentCol = -1;
        int authorCol = -1;
        int dynastyCol = -1;
        int annotationCol = -1;
        int markerCol = -1;
        int typeCol = -1;
        int translationCol = -1;
        int translatorCol = -1;
    }
}

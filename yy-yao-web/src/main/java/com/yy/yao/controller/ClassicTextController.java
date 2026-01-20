package com.yy.yao.controller;

import com.yy.yao.dto.ClassicTextDetailDTO;
import com.yy.yao.entity.Annotation;
import com.yy.yao.entity.ClassicText;
import com.yy.yao.service.ClassicTextService;
import com.yy.yao.service.ExcelImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 经文查询API控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/classic")
@RequiredArgsConstructor
public class ClassicTextController {

    private final ClassicTextService classicTextService;
    private final ExcelImportService excelImportService;

    /**
     * 根据ID查询经文详情
     */
    @GetMapping("/text/{id}")
    public ResponseEntity<ClassicTextDetailDTO> getTextDetail(@PathVariable Long id) {
        log.info("查询经文详情: id={}", id);

        return classicTextService.getTextDetail(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据章节查询经文列表
     */
    @GetMapping("/chapter/{chapter}")
    public ResponseEntity<List<ClassicTextDetailDTO>> getTextsByChapter(@PathVariable String chapter) {
        log.info("查询章节: {}", chapter);

        List<ClassicTextDetailDTO> texts = classicTextService.getTextsByChapter(chapter);
        return ResponseEntity.ok(texts);
    }

    /**
     * 搜索经文
     */
    @GetMapping("/search")
    public ResponseEntity<List<ClassicText>> searchTexts(@RequestParam String keyword) {
        log.info("搜索经文: keyword={}", keyword);

        List<ClassicText> results = classicTextService.searchTexts(keyword);
        return ResponseEntity.ok(results);
    }

    /**
     * 分页搜索经文
     */
    @GetMapping("/search/page")
    public ResponseEntity<Page<ClassicText>> searchTextsPage(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("分页搜索经文: keyword={}, page={}, size={}", keyword, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<ClassicText> results = classicTextService.searchTextsPage(keyword, pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * 查询注释
     */
    @GetMapping("/annotation/{textId}/{marker}")
    public ResponseEntity<Annotation> getAnnotation(
            @PathVariable Long textId,
            @PathVariable String marker) {

        log.info("查询注释: textId={}, marker={}", textId, marker);

        return classicTextService.getAnnotationByMarker(textId, marker)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 获取所有章节列表
     */
    @GetMapping("/chapters")
    public ResponseEntity<List<String>> getAllChapters() {
        log.info("获取所有章节列表");

        List<String> chapters = classicTextService.getAllChapters();
        return ResponseEntity.ok(chapters);
    }

    /**
     * 从Excel导入数据
     */
    @PostMapping("/import")
    public ResponseEntity<ImportResult> importFromExcel(@RequestParam String filePath) {
        log.info("导入Excel数据: {}", filePath);

        try {
            int imported = excelImportService.importFromExcel(filePath);
            return ResponseEntity.ok(new ImportResult(true, imported, "导入成功"));
        } catch (IOException e) {
            log.error("导入失败", e);
            return ResponseEntity.badRequest()
                    .body(new ImportResult(false, 0, "导入失败: " + e.getMessage()));
        }
    }

    /**
     * 导入结果
     */
    record ImportResult(boolean success, int count, String message) {
    }
}

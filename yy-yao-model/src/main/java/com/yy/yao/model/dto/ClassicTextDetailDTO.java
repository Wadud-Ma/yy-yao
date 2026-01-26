package com.yy.yao.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 经文详情DTO - 包含原文、注释和翻译
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassicTextDetailDTO {

    /**
     * 原文ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 章节
     */
    private String chapter;

    /**
     * 原文内容
     */
    private String content;

    /**
     * 作者
     */
    private String author;

    /**
     * 朝代
     */
    private String dynasty;

    /**
     * 注释列表
     */
    private List<AnnotationDTO> annotations;

    /**
     * 翻译列表
     */
    private List<TranslationDTO> translations;

    /**
     * 注释DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnnotationDTO {
        private Long id;
        private String marker;
        private String content;
        private String type;
    }

    /**
     * 翻译DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TranslationDTO {
        private Long id;
        private String content;
        private String translator;
    }
}

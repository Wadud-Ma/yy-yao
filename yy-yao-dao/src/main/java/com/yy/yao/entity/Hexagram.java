package com.yy.yao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 六十四卦实体
 * 对应数据库表: hexagrams
 */
@Entity
@Table(name = "hexagrams")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hexagram {

    /**
     * 卦序编号 (1-64)
     */
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 卦名
     */
    @Column(name = "name", nullable = false, length = 4)
    private String name;

    /**
     * 六爻符号，111111表示乾卦（从下到上）
     */
    @Column(name = "symbol", nullable = false, length = 6)
    private String symbol;

    /**
     * Unicode卦象符号，如 ☰☰
     */
    @Column(name = "unicode_symbol", length = 10)
    private String unicodeSymbol;

    /**
     * 上卦ID
     */
    @Column(name = "upper_trigram_id", nullable = false)
    private Integer upperTrigramId;

    /**
     * 下卦ID
     */
    @Column(name = "lower_trigram_id", nullable = false)
    private Integer lowerTrigramId;

    /**
     * 完整卦名，如 乾为天
     */
    @Column(name = "full_name", length = 20)
    private String fullName;

    /**
     * 卦辞原文
     */
    @Column(name = "judgment", columnDefinition = "TEXT", nullable = false)
    private String judgment;

    /**
     * 卦辞注释
     */
    @Column(name = "judgment_annotation", columnDefinition = "TEXT")
    private String judgmentAnnotation;

    /**
     * 卦辞译文
     */
    @Column(name = "judgment_translation", columnDefinition = "TEXT")
    private String judgmentTranslation;

    /**
     * 彖传原文
     */
    @Column(name = "tuan_text", columnDefinition = "TEXT")
    private String tuanText;

    /**
     * 彖传注释
     */
    @Column(name = "tuan_annotation", columnDefinition = "TEXT")
    private String tuanAnnotation;

    /**
     * 彖传译文
     */
    @Column(name = "tuan_translation", columnDefinition = "TEXT")
    private String tuanTranslation;

    /**
     * 大象原文
     */
    @Column(name = "image_text", columnDefinition = "TEXT")
    private String imageText;

    /**
     * 大象注释
     */
    @Column(name = "image_annotation", columnDefinition = "TEXT")
    private String imageAnnotation;

    /**
     * 大象译文
     */
    @Column(name = "image_translation", columnDefinition = "TEXT")
    private String imageTranslation;

    /**
     * 象征意涵
     */
    @Column(name = "symbolism", length = 255)
    private String symbolism;

    /**
     * 人生启示
     */
    @Column(name = "life_lesson", columnDefinition = "TEXT")
    private String lifeLesson;

    /**
     * 实用建议
     */
    @Column(name = "advice", columnDefinition = "TEXT")
    private String advice;
}

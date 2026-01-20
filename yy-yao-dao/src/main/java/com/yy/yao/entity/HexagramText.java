package com.yy.yao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 卦象文本实体 - 包含原文、彖传、象传、译文、注释等
 */
@Entity
@Table(name = "hexagram_text", indexes = {
        @Index(name = "idx_hexagram_text_hexagram_id", columnList = "hexagram_id", unique = true)
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HexagramText {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的卦象ID
     */
    @Column(name = "hexagram_id", nullable = false, unique = true)
    private Long hexagramId;

    /**
     * 卦辞原文
     */
    @Column(name = "original_text", columnDefinition = "TEXT")
    private String originalText;

    /**
     * 《彖》曰原文
     */
    @Column(name = "tuan_text", columnDefinition = "TEXT")
    private String tuanText;

    /**
     * 彖传译文
     */
    @Column(name = "tuan_translation", columnDefinition = "TEXT")
    private String tuanTranslation;

    /**
     * 彖传注释
     */
    @Column(name = "tuan_annotation", columnDefinition = "TEXT")
    private String tuanAnnotation;

    /**
     * 《象》曰原文 (大象传)
     */
    @Column(name = "xiang_text", columnDefinition = "TEXT")
    private String xiangText;

    /**
     * 象传译文
     */
    @Column(name = "xiang_translation", columnDefinition = "TEXT")
    private String xiangTranslation;

    /**
     * 象传注释
     */
    @Column(name = "xiang_annotation", columnDefinition = "TEXT")
    private String xiangAnnotation;

    /**
     * 卦辞译文
     */
    @Column(name = "overall_translation", columnDefinition = "TEXT")
    private String overallTranslation;

    /**
     * 卦辞注释
     */
    @Column(name = "overall_annotation", columnDefinition = "TEXT")
    private String overallAnnotation;

    /**
     * 综合寓意
     */
    @Column(name = "comprehensive_meaning", columnDefinition = "TEXT")
    private String comprehensiveMeaning;

    /**
     * 应用指导
     */
    @Column(name = "application_guidance", columnDefinition = "TEXT")
    private String applicationGuidance;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

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
 * 爻辞文本实体
 */
@Entity
@Table(name = "line_text", indexes = {
        @Index(name = "idx_line_hexagram_position", columnList = "hexagram_id,position")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineText {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的卦象ID
     */
    @Column(name = "hexagram_id", nullable = false)
    private Long hexagramId;

    /**
     * 爻位 (1-6, 从下到上)
     */
    @Column(nullable = false)
    private Integer position;

    /**
     * 爻名 (如: 初九, 六二)
     */
    @Column(name = "line_name", length = 20)
    private String lineName;

    /**
     * 是否为阳爻
     */
    @Column(name = "is_yang")
    private Boolean isYang;

    /**
     * 爻辞原文
     */
    @Column(name = "original_text", columnDefinition = "TEXT")
    private String originalText;

    /**
     * 小象传原文
     */
    @Column(name = "xiang_text", columnDefinition = "TEXT")
    private String xiangText;

    /**
     * 小象传译文
     */
    @Column(name = "xiang_translation", columnDefinition = "TEXT")
    private String xiangTranslation;

    /**
     * 小象传注释
     */
    @Column(name = "xiang_annotation", columnDefinition = "TEXT")
    private String xiangAnnotation;

    /**
     * 爻辞译文
     */
    @Column(columnDefinition = "TEXT")
    private String translation;

    /**
     * 爻辞注释
     */
    @Column(columnDefinition = "TEXT")
    private String annotation;

    /**
     * 爻义
     */
    @Column(columnDefinition = "TEXT")
    private String meaning;

    /**
     * 实际应用
     */
    @Column(columnDefinition = "TEXT")
    private String application;

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

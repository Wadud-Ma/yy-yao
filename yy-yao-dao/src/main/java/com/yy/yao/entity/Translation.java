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
 * 白话释义表 - 存储对经段或译句的现代翻译
 */
@Entity
@Table(name = "translation", indexes = {
        @Index(name = "idx_translation_text_id", columnList = "text_id"),
        @Index(name = "idx_translation_translator", columnList = "translator")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的原文ID
     */
    @Column(name = "text_id", nullable = false)
    private Long textId;

    /**
     * 翻译内容
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 译者
     */
    @Column(length = 50)
    private String translator;

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

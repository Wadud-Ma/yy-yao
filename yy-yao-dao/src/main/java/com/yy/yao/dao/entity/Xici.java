package com.yy.yao.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


/**
 * 系辞传实体
 * 对应数据库表: xici
 */
@Entity
@Table(name = "xici")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Xici {

    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 篇章：上篇/下篇
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "part", nullable = false)
    private Part part;

    /**
     * 章节号
     */
    @Column(name = "chapter")
    private Integer chapter;

    /**
     * 段落号
     */
    @Column(name = "paragraph")
    private Integer paragraph;

    /**
     * 原文
     */
    @Column(name = "text", columnDefinition = "TEXT", nullable = false)
    private String text;

    /**
     * 注释
     */
    @Column(name = "annotation", columnDefinition = "TEXT")
    private String annotation;

    /**
     * 译文
     */
    @Column(name = "translation", columnDefinition = "TEXT")
    private String translation;

    /**
     * 排序
     */
    @Column(name = "sort_order")
    private Integer sortOrder;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 篇章枚举
     */
    public enum Part {
        upper,  // 上篇
        lower   // 下篇
    }
}
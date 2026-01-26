package com.yy.yao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文言传实体（仅乾坤两卦）
 * 对应数据库表: wenyan
 */
@Entity
@Table(name = "wenyan")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wenyan {

    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 所属卦ID (1=乾, 2=坤)
     */
    @Column(name = "hexagram_id", nullable = false)
    private Integer hexagramId;

    /**
     * 段落标识
     */
    @Column(name = "section", length = 20)
    private String section;

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
}

package com.yy.yao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


/**
 * 杂卦传实体
 * 对应数据库表: zagua
 */
@Entity
@Table(name = "zagua")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Zagua {

    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 关联卦象ID
     */
    @Column(name = "hexagram_id")
    private Integer hexagramId;

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
}

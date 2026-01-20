package com.yy.yao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 八卦基础信息实体
 */
@Entity
@Table(name = "trigram_info", indexes = {
        @Index(name = "idx_trigram_name", columnList = "name", unique = true)
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrigramInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 卦名 (乾、坤、震、巽、坎、离、艮、兑)
     */
    @Column(nullable = false, unique = true, length = 10)
    private String name;

    /**
     * 卦符号
     */
    @Column(length = 5)
    private String symbol;

    /**
     * 二进制表示 (3位)
     */
    @Column(name = "binary_code", length = 3)
    private String binaryCode;

    /**
     * 自然属性 (天、地、雷、风、水、火、山、泽)
     */
    @Column(length = 20)
    private String nature;

    /**
     * 特质 (健、顺、动、入、陷、丽、止、说)
     */
    @Column(length = 20)
    private String attribute;

    /**
     * 家族成员 (父、母、长男、长女、中男、中女、少男、少女)
     */
    @Column(name = "family_member", length = 20)
    private String familyMember;

    /**
     * 方位
     */
    @Column(length = 20)
    private String direction;

    /**
     * 季节
     */
    @Column(length = 20)
    private String season;

    /**
     * 人体部位
     */
    @Column(name = "body_part", length = 20)
    private String bodyPart;

    /**
     * 动物
     */
    @Column(length = 20)
    private String animal;

    /**
     * 颜色
     */
    @Column(length = 20)
    private String color;

    /**
     * 数字
     */
    @Column(length = 20)
    private String number;

    /**
     * 描述
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

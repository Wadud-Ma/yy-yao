package com.yy.yao.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 八卦实体
 * 对应数据库表: trigrams
 */
@Entity
@Table(name = "trigrams")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trigram {

    /**
     * 八卦编号 (1-8)
     */
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 卦名: 乾、坤、震、巽、坎、离、艮、兑
     */
    @Column(name = "name", nullable = false, length = 4)
    private String name;

    /**
     * 三爻符号，如 111 表示乾
     */
    @Column(name = "symbol", nullable = false, length = 3)
    private String symbol;

    /**
     * Unicode符号: ☰☷☳☴☵☲☶☱
     */
    @Column(name = "unicode_symbol", nullable = false, length = 1)
    private String unicodeSymbol;

    /**
     * 象征: 天、地、雷、风、水、火、山、泽
     */
    @Column(name = "nature", length = 10)
    private String nature;

    /**
     * 属性: 刚健、柔顺、动、入、陷、丽、止、悦
     */
    @Column(name = "attribute", length = 20)
    private String attribute;

    /**
     * 方位
     */
    @Column(name = "direction", length = 10)
    private String direction;

    /**
     * 季节
     */
    @Column(name = "season", length = 20)
    private String season;

    /**
     * 家庭成员: 父、母、长男、长女、中男、中女、少男、少女
     */
    @Column(name = "family_member", length = 10)
    private String familyMember;

    /**
     * 身体部位
     */
    @Column(name = "body_part", length = 20)
    private String bodyPart;

    /**
     * 动物
     */
    @Column(name = "animal", length = 20)
    private String animal;
}

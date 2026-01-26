package com.yy.yao.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 爻辞实体
 * 对应数据库表: hexagram_lines
 */
@Entity
@Table(name = "hexagram_lines", indexes = {
        @Index(name = "idx_hexagram_position", columnList = "hexagram_id, position", unique = true)
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HexagramLine {

    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 所属卦ID (1-64)
     */
    @Column(name = "hexagram_id", nullable = false)
    private Integer hexagramId;

    /**
     * 爻位 (1-6，从下往上)
     */
    @Column(name = "position", nullable = false)
    private Integer position;

    /**
     * 爻名: 初九、九二、六三等
     */
    @Column(name = "name", nullable = false, length = 10)
    private String name;

    /**
     * 爻性: yang=阳爻, yin=阴爻
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LineType type;

    /**
     * 爻辞原文
     */
    @Column(name = "line_text", columnDefinition = "TEXT", nullable = false)
    private String lineText;

    /**
     * 爻辞注释
     */
    @Column(name = "line_annotation", columnDefinition = "TEXT")
    private String lineAnnotation;

    /**
     * 爻辞译文
     */
    @Column(name = "line_translation", columnDefinition = "TEXT")
    private String lineTranslation;

    /**
     * 小象原文
     */
    @Column(name = "xiang_text", columnDefinition = "TEXT")
    private String xiangText;

    /**
     * 小象注释
     */
    @Column(name = "xiang_annotation", columnDefinition = "TEXT")
    private String xiangAnnotation;

    /**
     * 小象译文
     */
    @Column(name = "xiang_translation", columnDefinition = "TEXT")
    private String xiangTranslation;

    /**
     * 爻性枚举
     */
    public enum LineType {
        yang,  // 阳爻
        yin    // 阴爻
    }
}

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
 * 卦象基础信息实体
 */
@Entity
@Table(name = "hexagram_info", indexes = {
        @Index(name = "idx_hexagram_number", columnList = "number", unique = true),
        @Index(name = "idx_hexagram_name", columnList = "name"),
        @Index(name = "idx_hexagram_binary", columnList = "binary_code")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HexagramInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 卦序号 (1-64)
     */
    @Column(nullable = false, unique = true)
    private Integer number;

    /**
     * 卦名
     */
    @Column(nullable = false, length = 10)
    private String name;

    /**
     * 卦符号
     */
    @Column(length = 10)
    private String symbol;

    /**
     * 二进制表示 (6位,从下到上)
     */
    @Column(name = "binary_code", nullable = false, length = 6)
    private String binaryCode;

    /**
     * 上卦
     */
    @Column(name = "upper_trigram", length = 10)
    private String upperTrigram;

    /**
     * 下卦
     */
    @Column(name = "lower_trigram", length = 10)
    private String lowerTrigram;

    /**
     * 卦象属性 (如: 天:健, 地:顺)
     */
    @Column(name = "trigram_nature", length = 50)
    private String trigramNature;

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

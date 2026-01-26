package com.yy.yao.dao.entity;

import com.yy.yao.model.domain.DivinationMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 占卜记录实体
 * 对应数据库表: divination_records
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "divination_records", indexes = {
        @Index(name = "idx_user", columnList = "user_id"),
        @Index(name = "idx_created", columnList = "created_at")
})
public class DivinationRecord {

    /**
     * 占卜记录ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 占卜方式
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "method", length = 20)
    private DivinationMethod method;

    /**
     * 所问之事
     */
    @Column(name = "question", length = 500)
    private String question;

    /**
     * 6爻数值 JSON格式: [7,8,9,7,6,8]
     */
    @Column(name = "lines", columnDefinition = "JSON")
    private String lines;

    /**
     * 本卦ID (1-64)
     */
    @Column(name = "original_hexagram_id", nullable = false)
    private Integer originalHexagramId;

    /**
     * 变卦ID（无变爻则为NULL）
     */
    @Column(name = "changed_hexagram_id")
    private Integer changedHexagramId;

    /**
     * 变爻位置 JSON格式: [3,5]
     */
    @Column(name = "changing_lines", columnDefinition = "JSON")
    private String changingLines;

    /**
     * AI完整解读
     */
    @Column(name = "ai_interpretation", columnDefinition = "TEXT")
    private String aiInterpretation;

    /**
     * 是否收藏
     */
    @Builder.Default
    @Column(name = "is_favorited", nullable = false)
    private Boolean isFavorited = false;

    /**
     * 用户备注
     */
    @Column(name = "remark", length = 1000)
    private String remark;

    /**
     * 占卜解读
     */
    @Column(name = "interpretation", columnDefinition = "TEXT")
    private String interpretation;

    /**
     * 本卦名称
     */
    @Column(name = "original_hexagram_name", length = 10)
    private String originalHexagramName;

    /**
     * 本卦二进制
     */
    @Column(name = "original_hexagram_binary", length = 6)
    private String originalHexagramBinary;

    /**
     * 变卦名称
     */
    @Column(name = "changed_hexagram_name", length = 10)
    private String changedHexagramName;

    /**
     * 变卦二进制
     */
    @Column(name = "changed_hexagram_binary", length = 6)
    private String changedHexagramBinary;

    /**
     * 六爻详细信息 JSON
     */
    @Column(name = "lines_detail", columnDefinition = "JSON")
    private String linesDetail;

    /**
     * 是否使用AI
     */
    @Builder.Default
    @Column(name = "used_ai", nullable = false)
    private Boolean usedAi = false;

    /**
     * 占卜时间
     */
    @Column(name = "divination_time")
    private LocalDateTime divinationTime;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

package com.yy.yao.entity;

import com.yy.yao.model.DivinationResult;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 占卜记录实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "divination_record", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_openid", columnList = "openid"),
        @Index(name = "idx_created_at", columnList = "created_at"),
        @Index(name = "idx_original_hexagram", columnList = "original_hexagram_id")
})
public class DivinationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID (关联 mini_program_user)
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户OpenID (冗余字段，便于查询)
     */
    @Column(name = "openid", length = 100, nullable = false)
    private String openid;

    /**
     * 问题/所求之事
     */
    @Column(name = "question", columnDefinition = "TEXT", nullable = false)
    private String question;

    /**
     * 占卜方法
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "divination_method", length = 20, nullable = false)
    private DivinationResult.DivinationMethod divinationMethod;

    /**
     * 本卦ID
     */
    @Column(name = "original_hexagram_id", nullable = false)
    private Integer originalHexagramId;

    /**
     * 本卦名称
     */
    @Column(name = "original_hexagram_name", length = 50)
    private String originalHexagramName;

    /**
     * 本卦二进制符号
     */
    @Column(name = "original_hexagram_binary", length = 10)
    private String originalHexagramBinary;

    /**
     * 变卦ID (如果有动爻)
     */
    @Column(name = "changed_hexagram_id")
    private Integer changedHexagramId;

    /**
     * 变卦名称
     */
    @Column(name = "changed_hexagram_name", length = 50)
    private String changedHexagramName;

    /**
     * 变卦二进制符号
     */
    @Column(name = "changed_hexagram_binary", length = 10)
    private String changedHexagramBinary;

    /**
     * 动爻列表 (JSON格式: [1,3,5])
     */
    @Column(name = "changing_lines", length = 50)
    private String changingLines;

    /**
     * 六爻详细信息 (JSON格式)
     */
    @Column(name = "lines_detail", columnDefinition = "TEXT")
    private String linesDetail;

    /**
     * 基础解卦
     */
    @Column(name = "interpretation", columnDefinition = "TEXT")
    private String interpretation;

    /**
     * AI智能解读
     */
    @Column(name = "ai_interpretation", columnDefinition = "TEXT")
    private String aiInterpretation;

    /**
     * 是否使用AI解读
     */
    @Column(name = "used_ai", nullable = false)
    private Boolean usedAi = false;

    /**
     * 占卜时间
     */
    @Column(name = "divination_time", nullable = false)
    private LocalDateTime divinationTime;

    /**
     * 创建时间
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * IP地址
     */
    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    /**
     * 用户代理
     */
    @Column(name = "user_agent", length = 500)
    private String userAgent;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    /**
     * 是否收藏
     */
    @Column(name = "is_favorite", nullable = false)
    private Boolean isFavorite = false;

    /**
     * 标签 (JSON格式: ["工作", "感情"])
     */
    @Column(name = "tags", length = 200)
    private String tags;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (divinationTime == null) {
            divinationTime = LocalDateTime.now();
        }
    }
}

package com.yy.yao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 小程序用户实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mini_program_user", indexes = {
        @Index(name = "idx_openid", columnList = "openid"),
        @Index(name = "idx_union_id", columnList = "union_id"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
public class MiniProgramUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 微信小程序 OpenID (唯一标识)
     */
    @Column(name = "openid", nullable = false, unique = true, length = 100)
    private String openid;

    /**
     * 微信 UnionID (跨应用唯一标识)
     */
    @Column(name = "union_id", length = 100)
    private String unionId;

    /**
     * 微信昵称
     */
    @Column(name = "nickname", length = 100)
    private String nickname;

    /**
     * 头像URL
     */
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    /**
     * 性别 (0-未知, 1-男, 2-女)
     */
    @Column(name = "gender")
    private Integer gender;

    /**
     * 国家
     */
    @Column(name = "country", length = 50)
    private String country;

    /**
     * 省份
     */
    @Column(name = "province", length = 50)
    private String province;

    /**
     * 城市
     */
    @Column(name = "city", length = 50)
    private String city;

    /**
     * 手机号
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * 会话密钥
     */
    @Column(name = "session_key", length = 100)
    private String sessionKey;

    /**
     * 今日占卜次数
     */
    @Column(name = "today_divination_count", nullable = false)
    private Integer todayDivinationCount = 0;

    /**
     * 最后占卜日期
     */
    @Column(name = "last_divination_date")
    private LocalDateTime lastDivinationDate;

    /**
     * 总占卜次数
     */
    @Column(name = "total_divination_count", nullable = false)
    private Integer totalDivinationCount = 0;

    /**
     * 是否启用
     */
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    /**
     * 创建时间
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 最后登录时间
     */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

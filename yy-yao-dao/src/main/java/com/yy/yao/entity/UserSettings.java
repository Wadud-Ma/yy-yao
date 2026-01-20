package com.yy.yao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 用户设置实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_settings", indexes = {
        @Index(name = "idx_openid", columnList = "openid")
})
public class UserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 微信小程序 OpenID (唯一标识)
     */
    @Column(unique = true, nullable = false, length = 100)
    private String openid;

    // 外观设置
    /**
     * 主题设置 (auto, light, dark)
     */
    @Column(length = 20)
    private String theme = "auto";

    /**
     * 是否使用繁体中文
     */
    @Column(name = "traditional_chinese")
    private Boolean traditionalChinese = false;

    // 功能设置
    /**
     * 是否启用声音
     */
    @Column(name = "sound_enabled")
    private Boolean soundEnabled = false;

    /**
     * 是否启用振动
     */
    @Column(name = "vibration_enabled")
    private Boolean vibrationEnabled = true;

    /**
     * 是否启用AI解读
     */
    @Column(name = "enable_ai")
    private Boolean enableAi = true;

    // 隐私设置
    /**
     * 是否自动保存历史记录
     */
    @Column(name = "auto_save_history")
    private Boolean autoSaveHistory = true;

    /**
     * 是否匿名分享
     */
    @Column(name = "share_anonymous")
    private Boolean shareAnonymous = true;

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

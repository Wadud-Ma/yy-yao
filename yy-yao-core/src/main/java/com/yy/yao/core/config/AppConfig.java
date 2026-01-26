package com.yy.yao.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 应用配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    /**
     * 应用名称
     */
    private String name = "易经卜卦服务";

    /**
     * 应用版本
     */
    private String version = "1.0.0";

    /**
     * 管理员邮箱
     */
    private String adminEmail;

    /**
     * 是否启用认证
     */
    private boolean authEnabled = true;

    /**
     * 是否启用AI功能
     */
    private boolean aiEnabled = false;

    /**
     * 最大卜卦次数限制 (每个用户每天)
     */
    private int maxDivinationsPerDay = 10;
}

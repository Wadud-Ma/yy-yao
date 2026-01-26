package com.yy.yao.web.controller;

import com.yy.yao.core.config.AppConfig;
import com.yy.yao.model.dto.ApiResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统控制器 - 健康检查和监控
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SystemController {

    private final DataSource dataSource;
    private final AppConfig appConfig;

    /**
     * 健康检查（前端 API 规范格式）
     */
    @GetMapping("/health")
    public ResponseEntity<HealthCheckResponse> health() {
        HealthCheckResponse response = new HealthCheckResponse();
        response.setOk(true);
        response.setTimestamp(System.currentTimeMillis());
        response.setVersion(appConfig.getVersion() != null ? appConfig.getVersion() : "1.0.0");

        // 检查数据库连接
        try (Connection conn = dataSource.getConnection()) {
            if (!conn.isValid(2)) {
                response.setOk(false);
            }
        } catch (Exception e) {
            log.error("数据库健康检查失败", e);
            response.setOk(false);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 详细健康检查
     */
    @GetMapping("/health/detail")
    public ResponseEntity<ApiResponse<HealthStatus>> healthDetail() {
        HealthStatus status = new HealthStatus();
        status.setStatus("UP");
        status.setTimestamp(LocalDateTime.now());

        // 检查数据库连接
        try (Connection conn = dataSource.getConnection()) {
            status.getDependencies().put("database", conn.isValid(2) ? "UP" : "DOWN");
        } catch (Exception e) {
            log.error("数据库健康检查失败", e);
            status.getDependencies().put("database", "DOWN");
            status.setStatus("DEGRADED");
        }
        return ResponseEntity.ok(ApiResponse.success(status));
    }

    /**
     * 系统信息
     */
    @GetMapping("/system/info")
    public ResponseEntity<ApiResponse<SystemInfo>> systemInfo() {
        SystemInfo info = new SystemInfo();
        info.setApplicationName(appConfig.getName());
        info.setVersion(appConfig.getVersion());
        info.setJavaVersion(System.getProperty("java.version"));
        info.setOsName(System.getProperty("os.name"));
        info.setOsVersion(System.getProperty("os.version"));
        info.setAuthEnabled(appConfig.isAuthEnabled());
        info.setAiEnabled(appConfig.isAiEnabled());

        return ResponseEntity.ok(ApiResponse.success(info));
    }

    /**
     * 系统统计
     */
    @GetMapping("/system/stats")
    public ResponseEntity<ApiResponse<SystemStats>> systemStats() {
        Runtime runtime = Runtime.getRuntime();

        SystemStats stats = new SystemStats();
        stats.setTotalMemory(runtime.totalMemory() / 1024 / 1024); // MB
        stats.setFreeMemory(runtime.freeMemory() / 1024 / 1024);   // MB
        stats.setMaxMemory(runtime.maxMemory() / 1024 / 1024);     // MB
        stats.setUsedMemory((runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024); // MB
        stats.setAvailableProcessors(runtime.availableProcessors());
        stats.setUptime(System.currentTimeMillis());

        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    // ========== DTO ==========

    @Data
    public static class HealthStatus {
        private String status = "UP";
        private LocalDateTime timestamp;
        private Map<String, String> dependencies = new HashMap<>();
    }

    @Data
    public static class SystemInfo {
        private String applicationName;
        private String version;
        private String javaVersion;
        private String osName;
        private String osVersion;
        private boolean authEnabled;
        private boolean aiEnabled;
    }

    @Data
    public static class SystemStats {
        private long totalMemory;
        private long freeMemory;
        private long usedMemory;
        private long maxMemory;
        private int availableProcessors;
        private long uptime;
    }

    @Data
    public static class HealthCheckResponse {
        private boolean ok;
        private long timestamp;
        private String version;
    }
}

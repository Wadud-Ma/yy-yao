package com.yy.yao.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

/**
 * 本地文件存储实现
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "local", matchIfMissing = true)
public class LocalStorageService implements StorageService {

    @Value("${storage.local.base-path:./uploads}")
    private String basePath;

    @Value("${storage.local.base-url:http://localhost:8080/uploads}")
    private String baseUrl;

    @Override
    public String store(String path, InputStream inputStream, String contentType, Map<String, String> metadata) throws Exception {
        Path fullPath = getFullPath(path);

        // 创建目录
        Files.createDirectories(fullPath.getParent());

        // 复制文件
        Files.copy(inputStream, fullPath, StandardCopyOption.REPLACE_EXISTING);

        log.info("文件已保存: {}", fullPath);
        return getUrl(path);
    }

    @Override
    public String store(String path, byte[] data, String contentType) throws Exception {
        Path fullPath = getFullPath(path);

        // 创建目录
        Files.createDirectories(fullPath.getParent());

        // 写入文件
        Files.write(fullPath, data);

        log.info("文件已保存: {}", fullPath);
        return getUrl(path);
    }

    @Override
    public String getUrl(String path) {
        // 规范化路径
        String normalizedPath = normalizePath(path);
        return baseUrl + "/" + normalizedPath;
    }

    @Override
    public boolean delete(String path) {
        try {
            Path fullPath = getFullPath(path);
            boolean deleted = Files.deleteIfExists(fullPath);
            if (deleted) {
                log.info("文件已删除: {}", fullPath);
            }
            return deleted;
        } catch (Exception e) {
            log.error("删除文件失败: {}", path, e);
            return false;
        }
    }

    @Override
    public boolean exists(String path) {
        Path fullPath = getFullPath(path);
        return Files.exists(fullPath);
    }

    @Override
    public long getSize(String path) {
        try {
            Path fullPath = getFullPath(path);
            if (Files.exists(fullPath)) {
                return Files.size(fullPath);
            }
        } catch (Exception e) {
            log.error("获取文件大小失败: {}", path, e);
        }
        return -1;
    }

    /**
     * 获取完整文件路径
     */
    private Path getFullPath(String path) {
        String normalizedPath = normalizePath(path);
        return Paths.get(basePath, normalizedPath);
    }

    /**
     * 规范化路径 (移除前导斜杠)
     */
    private String normalizePath(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        // 移除前导斜杠
        while (path.startsWith("/")) {
            path = path.substring(1);
        }

        // 防止路径遍历攻击
        if (path.contains("..")) {
            throw new IllegalArgumentException("非法文件路径");
        }

        return path;
    }

    /**
     * 初始化存储目录
     */
    public void init() {
        try {
            Path path = Paths.get(basePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                log.info("本地存储目录已创建: {}", basePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("无法创建存储目录: " + basePath, e);
        }
    }
}

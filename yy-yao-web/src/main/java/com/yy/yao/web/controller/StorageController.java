package com.yy.yao.web.controller;

import com.yy.yao.model.dto.ApiResponse;
import com.yy.yao.service.impl.storage.StorageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件存储控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_TYPES = {
            "image/jpeg", "image/png", "image/gif", "image/webp",
            "application/pdf", "text/plain"
    };

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<UploadResponse>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "path", required = false) String customPath,
            @RequestParam(value = "contentType", required = false) String customContentType,
            Authentication authentication) {

        try {
            // 验证文件
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "文件不能为空"));
            }

            // 检查文件大小
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "文件大小不能超过 10MB"));
            }

            // 确定内容类型
            String contentType = customContentType != null ? customContentType : file.getContentType();

            // 检查文件类型
            if (!isAllowedType(contentType)) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "不支持的文件类型: " + contentType));
            }

            // 生成文件路径
            String username = authentication != null ? authentication.getName() : "anonymous";
            String path;

            if (customPath != null && !customPath.isEmpty()) {
                // 使用自定义路径
                path = customPath;
            } else {
                // 生成默认路径
                String fileName = generateFileName(file.getOriginalFilename());
                path = String.format("uploads/%s/%s/%s",
                        username,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                        fileName);
            }

            // 保存文件
            Map<String, String> metadata = new HashMap<>();
            metadata.put("originalName", file.getOriginalFilename());
            metadata.put("uploader", username);
            metadata.put("uploadTime", LocalDateTime.now().toString());
            metadata.put("size", String.valueOf(file.getSize()));

            String url = storageService.store(path, file.getInputStream(), contentType, metadata);

            UploadResponse response = new UploadResponse(path, url);

            log.info("文件上传成功: user={}, path={}, size={}", username, path, file.getSize());
            return ResponseEntity.ok(ApiResponse.success("文件上传成功", response));

        } catch (Exception e) {
            log.error("文件上传失败", e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "文件上传失败: " + e.getMessage()));
        }
    }

    /**
     * 获取下载 URL
     */
    @GetMapping("/download-url")
    public ResponseEntity<ApiResponse<DownloadUrlResponse>> getDownloadUrl(
            @RequestParam("key") String key) {

        try {
            // 检查文件是否存在
            if (!storageService.exists(key)) {
                return ResponseEntity.status(404)
                        .body(ApiResponse.error(404, "文件不存在"));
            }

            // 获取访问 URL
            String url = storageService.getUrl(key);

            DownloadUrlResponse response = new DownloadUrlResponse(url);

            log.info("获取下载 URL 成功: key={}", key);
            return ResponseEntity.ok(ApiResponse.success(response));

        } catch (Exception e) {
            log.error("获取下载 URL 失败", e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "获取下载 URL 失败: " + e.getMessage()));
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<DeleteResponse>> deleteFile(
            @Valid @RequestBody DeleteRequest request,
            Authentication authentication) {

        try {
            String key = request.getKey();

            // 检查文件是否存在
            if (!storageService.exists(key)) {
                return ResponseEntity.status(404)
                        .body(ApiResponse.error(404, "文件不存在"));
            }

            // 验证权限（简单实现：检查路径中是否包含用户名）
            if (authentication != null) {
                String username = authentication.getName();
                if (!key.contains(username) && !"admin".equals(username)) {
                    return ResponseEntity.status(403)
                            .body(ApiResponse.error(403, "无权删除此文件"));
                }
            }

            // 删除文件
            boolean deleted = storageService.delete(key);

            if (deleted) {
                DeleteResponse response = new DeleteResponse(true);
                log.info("文件删除成功: key={}", key);
                return ResponseEntity.ok(ApiResponse.success("文件删除成功", response));
            } else {
                return ResponseEntity.status(500)
                        .body(ApiResponse.error(500, "文件删除失败"));
            }

        } catch (Exception e) {
            log.error("文件删除失败", e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "文件删除失败: " + e.getMessage()));
        }
    }

    /**
     * 生成唯一文件名
     */
    private String generateFileName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }

    /**
     * 检查文件类型是否允许
     */
    private boolean isAllowedType(String contentType) {
        if (contentType == null) {
            return false;
        }
        for (String allowedType : ALLOWED_TYPES) {
            if (contentType.startsWith(allowedType)) {
                return true;
            }
        }
        return false;
    }

    // ========== DTO ==========

    @Data
    @AllArgsConstructor
    public static class UploadResponse {
        private String key;
        private String url;
    }

    @Data
    @AllArgsConstructor
    public static class DownloadUrlResponse {
        private String url;
    }

    @Data
    public static class DeleteRequest {
        @NotBlank(message = "文件 key 不能为空")
        private String key;
    }

    @Data
    @AllArgsConstructor
    public static class DeleteResponse {
        private boolean success;
    }
}

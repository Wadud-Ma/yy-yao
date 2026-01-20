package com.yy.yao.controller;

import com.yy.yao.dto.ApiResponse;
import com.yy.yao.service.storage.StorageService;
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
 * 文件上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final StorageService storageService;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_TYPES = {"image/jpeg", "image/png", "image/gif", "application/pdf"};

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "category", defaultValue = "general") String category,
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

            // 检查文件类型
            String contentType = file.getContentType();
            if (!isAllowedType(contentType)) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "不支持的文件类型: " + contentType));
            }

            // 生成文件路径
            String username = authentication != null ? authentication.getName() : "anonymous";
            String fileName = generateFileName(file.getOriginalFilename());
            String path = String.format("%s/%s/%s/%s",
                    category,
                    username,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                    fileName);

            // 保存文件
            Map<String, String> metadata = new HashMap<>();
            metadata.put("originalName", file.getOriginalFilename());
            metadata.put("uploader", username);
            metadata.put("uploadTime", LocalDateTime.now().toString());

            String url = storageService.store(path, file.getInputStream(), contentType, metadata);

            FileUploadResponse response = new FileUploadResponse(
                    path,
                    url,
                    file.getOriginalFilename(),
                    contentType,
                    file.getSize()
            );

            log.info("文件上传成功: user={}, path={}, size={}", username, path, file.getSize());
            return ResponseEntity.ok(ApiResponse.success("文件上传成功", response));

        } catch (Exception e) {
            log.error("文件上传失败", e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "文件上传失败: " + e.getMessage()));
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/{category}/{username}/**")
    public ResponseEntity<ApiResponse<Void>> deleteFile(
            @PathVariable String category,
            @PathVariable String username,
            Authentication authentication) {

        try {
            // 验证权限 (只能删除自己的文件)
            if (authentication == null || !authentication.getName().equals(username)) {
                return ResponseEntity.status(403)
                        .body(ApiResponse.error(403, "无权删除此文件"));
            }

            // 构造文件路径 (从请求中提取)
            // 这里简化处理,实际应该更严格
            String path = category + "/" + username + "/..."; // 实际路径需要完整提取

            boolean deleted = storageService.delete(path);

            if (deleted) {
                return ResponseEntity.ok(ApiResponse.success("文件删除成功", null));
            } else {
                return ResponseEntity.status(404)
                        .body(ApiResponse.error(404, "文件不存在"));
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
    public static class FileUploadResponse {
        private String path;
        private String url;
        private String originalName;
        private String contentType;
        private long size;
    }
}

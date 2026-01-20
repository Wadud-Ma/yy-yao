package com.yy.yao.controller;

import com.yy.yao.dto.ApiResponse;
import com.yy.yao.service.ImageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 图片生成控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /**
     * 生成图片
     */
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<ImageResponse>> generateImage(
            @Valid @RequestBody GenerateImageRequest request,
            Authentication authentication) {

        try {
            String username = authentication != null ? authentication.getName() : "anonymous";
            log.info("收到图片生成请求: user={}, prompt={}", username, request.getPrompt());

            // 调用图片生成服务
            ImageService.ImageGenerationResult result = imageService.generateImage(
                    request.getPrompt(),
                    request.getWidth(),
                    request.getHeight()
            );

            ImageResponse response = new ImageResponse(
                    result.getUrl(),
                    result.getWidth(),
                    result.getHeight()
            );

            log.info("图片生成成功: user={}, url={}", username, result.getUrl());
            return ResponseEntity.ok(ApiResponse.success("图片生成成功", response));

        } catch (IllegalStateException e) {
            log.error("图片生成服务不可用", e);
            return ResponseEntity.status(503)
                    .body(ApiResponse.error(503, e.getMessage()));
        } catch (Exception e) {
            log.error("图片生成失败", e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "图片生成失败: " + e.getMessage()));
        }
    }

    /**
     * 编辑图片
     */
    @PostMapping("/edit")
    public ResponseEntity<ApiResponse<ImageResponse>> editImage(
            @Valid @RequestBody EditImageRequest request,
            Authentication authentication) {

        try {
            String username = authentication != null ? authentication.getName() : "anonymous";
            log.info("收到图片编辑请求: user={}, prompt={}", username, request.getPrompt());

            // 调用图片编辑服务
            ImageService.ImageGenerationResult result = imageService.editImage(
                    request.getPrompt(),
                    request.getOriginalImageUrl(),
                    request.getMaskImageUrl()
            );

            ImageResponse response = new ImageResponse(
                    result.getUrl(),
                    result.getWidth(),
                    result.getHeight()
            );

            log.info("图片编辑成功: user={}, url={}", username, result.getUrl());
            return ResponseEntity.ok(ApiResponse.success("图片编辑成功", response));

        } catch (IllegalStateException e) {
            log.error("图片生成服务不可用", e);
            return ResponseEntity.status(503)
                    .body(ApiResponse.error(503, e.getMessage()));
        } catch (Exception e) {
            log.error("图片编辑失败", e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "图片编辑失败: " + e.getMessage()));
        }
    }

    // ========== DTO ==========

    @Data
    public static class GenerateImageRequest {
        @NotBlank(message = "提示词不能为空")
        private String prompt;

        @Min(256)
        @Max(2048)
        private Integer width = 512;

        @Min(256)
        @Max(2048)
        private Integer height = 512;
    }

    @Data
    public static class EditImageRequest {
        @NotBlank(message = "提示词不能为空")
        private String prompt;

        @NotBlank(message = "原始图片 URL 不能为空")
        private String originalImageUrl;

        private String maskImageUrl; // 可选
    }

    @Data
    @AllArgsConstructor
    public static class ImageResponse {
        private String url;
        private int width;
        private int height;
    }
}

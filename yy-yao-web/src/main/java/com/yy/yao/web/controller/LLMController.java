package com.yy.yao.web.controller;

import com.yy.yao.model.dto.ApiResponse;
import com.yy.yao.service.impl.LlmService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * LLM AI 控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/llm")
@RequiredArgsConstructor
public class LLMController {

    private final LlmService llmService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 调用 LLM
     */
    @PostMapping("/invoke")
    public ResponseEntity<ApiResponse<LLMResponse>> invoke(@Valid @RequestBody LLMRequest request) {
        log.info("收到 LLM 调用请求，消息数量: {}", request.getMessages().size());

        try {
            // 转换为 LlmService 的 ChatMessage 格式
            List<LlmService.ChatMessage> messages = request.getMessages().stream()
                    .map(msg -> new LlmService.ChatMessage(msg.getRole(), msg.getContent()))
                    .toList();

            // 调用 LLM 服务
            String content = llmService.chat(messages);

            // 构建响应
            LLMResponse response = new LLMResponse();
            response.setId("chatcmpl-" + System.currentTimeMillis());
            response.setModel("gpt-4");

            LLMResponse.Choice choice = new LLMResponse.Choice();
            choice.setIndex(0);
            choice.setMessage(new LLMResponse.Message("assistant", content));
            choice.setFinishReason("stop");
            response.setChoices(List.of(choice));

            LLMResponse.Usage usage = new LLMResponse.Usage();
            usage.setPromptTokens(estimateTokens(request.getMessages()));
            usage.setCompletionTokens(estimateTokens(content));
            usage.setTotalTokens(usage.getPromptTokens() + usage.getCompletionTokens());
            response.setUsage(usage);

            log.info("LLM 调用成功，生成内容长度: {}", content.length());
            return ResponseEntity.ok(ApiResponse.success(response));

        } catch (Exception e) {
            log.error("LLM 调用失败", e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error(500, "LLM 调用失败: " + e.getMessage()));
        }
    }

    /**
     * 流式调用 LLM (SSE)
     */
    @PostMapping(value = "/invoke-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter invokeStream(@Valid @RequestBody LLMRequest request) {
        log.info("收到 LLM 流式调用请求，消息数量: {}", request.getMessages().size());

        SseEmitter emitter = new SseEmitter(300000L); // 5分钟超时

        executorService.execute(() -> {
            try {
                // 转换为 LlmService 的 ChatMessage 格式
                List<LlmService.ChatMessage> messages = request.getMessages().stream()
                        .map(msg -> new LlmService.ChatMessage(msg.getRole(), msg.getContent()))
                        .toList();

                // 调用 LLM 服务获取完整响应
                String content = llmService.chat(messages);

                // 模拟流式输出（逐字发送）
                // 注意：真实的流式输出需要 LLM SDK 支持 streaming
                for (int i = 0; i < content.length(); i++) {
                    String delta = String.valueOf(content.charAt(i));
                    StreamDelta streamDelta = new StreamDelta(delta);
                    emitter.send(SseEmitter.event().data(streamDelta));

                    // 添加小延迟模拟流式效果
                    Thread.sleep(20);
                }

                // 发送完成信号
                emitter.send(SseEmitter.event().data("[DONE]"));
                emitter.complete();

                log.info("LLM 流式调用完成");

            } catch (IOException e) {
                log.error("SSE 发送失败", e);
                emitter.completeWithError(e);
            } catch (Exception e) {
                log.error("LLM 流式调用失败", e);
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }

    /**
     * 估算 token 数量（简单实现）
     */
    private int estimateTokens(List<MessageDTO> messages) {
        return messages.stream()
                .mapToInt(msg -> msg.getContent().length() / 4)
                .sum();
    }

    private int estimateTokens(String text) {
        return text.length() / 4;
    }

    // ========== DTO ==========

    @Data
    public static class LLMRequest {
        @NotNull(message = "消息列表不能为空")
        private List<MessageDTO> messages;

        @Min(1)
        @Max(4000)
        private Integer maxTokens = 1000;

        @Min(0)
        @Max(1)
        private Double temperature = 0.7;
    }

    @Data
    public static class MessageDTO {
        @NotNull(message = "角色不能为空")
        private String role; // system, user, assistant

        @NotNull(message = "内容不能为空")
        private String content;
    }

    @Data
    public static class LLMResponse {
        private String id;
        private String model;
        private List<Choice> choices;
        private Usage usage;

        @Data
        public static class Choice {
            private int index;
            private Message message;
            private String finishReason;
        }

        @Data
        public static class Message {
            private String role;
            private String content;

            public Message() {}

            public Message(String role, String content) {
                this.role = role;
                this.content = content;
            }
        }

        @Data
        public static class Usage {
            private int promptTokens;
            private int completionTokens;
            private int totalTokens;
        }
    }

    @Data
    public static class StreamDelta {
        private final String delta;

        public StreamDelta(String delta) {
            this.delta = delta;
        }
    }
}

package com.yy.yao.service.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring AI LLM 服务
 * 使用 Spring AI ChatClient 与 OpenAI API 集成
 */
@Slf4j
@Service
public class LlmService {

    @Value("${spring.ai.openai.api-key:}")
    private String apiKey;

    private final ChatClient chatClient;
    private final ChatModel chatModel;

    public LlmService(ChatModel chatModel) {
        this.chatModel = chatModel;
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    /**
     * 调用 LLM API - 使用 Spring AI ChatClient
     */
    public String chat(List<ChatMessage> messages) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("LLM API Key 未配置");
        }

        log.debug("LLM 请求消息数量: {}", messages.size());

        // 转换自定义 ChatMessage 为 Spring AI Message
        List<Message> springMessages = messages.stream()
                .map(msg -> {
                    if ("system".equals(msg.getRole())) {
                        return new SystemMessage(msg.getContent());
                    } else {
                        return new UserMessage(msg.getContent());
                    }
                })
                .collect(Collectors.toList());

        // 使用 ChatClient 进行调用
        String response = chatClient.prompt()
                .messages(springMessages)
                .call()
                .content();

        log.debug("LLM 响应: {}", response);
        return response;
    }

    /**
     * 简化的单轮对话
     */
    public String chat(String systemPrompt, String userMessage) {
        List<ChatMessage> messages = new ArrayList<>();
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            messages.add(new ChatMessage("system", systemPrompt));
        }
        messages.add(new ChatMessage("user", userMessage));
        return chat(messages);
    }

    /**
     * 检查 LLM 服务是否可用
     */
    public boolean isAvailable() {
        return apiKey != null && !apiKey.isEmpty();
    }

    // ========== 数据模型 ==========

    @Data
    public static class ChatMessage {
        private String role;
        private String content;

        public ChatMessage() {}

        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}

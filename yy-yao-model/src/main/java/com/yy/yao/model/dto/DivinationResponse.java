package com.yy.yao.model.dto;

import com.yy.yao.model.domain.DivinationMethod;
import com.yy.yao.model.domain.DivinationResult;
import com.yy.yao.model.domain.Hexagram;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 卜卦响应 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DivinationResponse {

    /**
     * 时间戳
     */
    private LocalDateTime timestamp;

    /**
     * 问题
     */
    private String question;

    /**
     * 本卦
     */
    private Hexagram originalHexagram;

    /**
     * 变卦
     */
    private Hexagram changedHexagram;

    /**
     * 动爻位置列表 (1-6)
     */
    private List<Integer> changingLines;

    /**
     * 卜卦方法
     */
    private DivinationMethod method;

    /**
     * 传统解卦
     */
    private String interpretation;

    /**
     * AI 解卦
     */
    private String aiInterpretation;

    /**
     * 从 DivinationResult 转换为 DivinationResponse
     */
    public static DivinationResponse fromDivinationResult(DivinationResult result) {
        return DivinationResponse.builder()
                .timestamp(result.getTimestamp())
                .question(result.getQuestion())
                .originalHexagram(result.getOriginalHexagram())
                .changedHexagram(result.getChangedHexagram())
                .changingLines(result.getChangingLines())
                .method(result.getMethod())
                .interpretation(result.getInterpretation())
                .aiInterpretation(result.getAiInterpretation())
                .build();
    }
}

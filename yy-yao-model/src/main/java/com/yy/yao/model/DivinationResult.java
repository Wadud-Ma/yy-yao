package com.yy.yao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 卜卦结果模型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DivinationResult {
    /**
     * 卜卦时间
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
     * 变卦 (如果有动爻)
     */
    private Hexagram changedHexagram;

    /**
     * 动爻列表 (从下到上, 1-6)
     */
    private List<Integer> changingLines;

    /**
     * 六爻结果 (从下到上)
     */
    private List<LineResult> lines;

    /**
     * 卜卦方法
     */
    private DivinationMethod method;

    /**
     * 解卦建议
     */
    private String interpretation;

    /**
     * AI智能解读
     */
    private String aiInterpretation;

    /**
     * 单爻结果
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LineResult {
        /**
         * 爻位 (1-6)
         */
        private int position;

        /**
         * 是否为阳爻
         */
        private boolean yang;

        /**
         * 是否为动爻
         */
        private boolean changing;

        /**
         * 爻值 (6=老阴动, 7=少阳, 8=少阴, 9=老阳动)
         */
        private int value;
    }

    /**
     * 卜卦方法枚举
     */
    public enum DivinationMethod {
        COIN("铜钱法"),
        YARROW("蓍草法"),
        NUMBER("数字法"),
        TIME("时间起卦法");

        private final String description;

        DivinationMethod(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}

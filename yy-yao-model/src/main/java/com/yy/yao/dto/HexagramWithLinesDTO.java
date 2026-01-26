package com.yy.yao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 包含爻辞的完整卦象 DTO
 * 用于详情页和占卜结果展示
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HexagramWithLinesDTO {

    /**
     * 卦序号 (1-64)
     */
    private Integer id;

    /**
     * 卦名
     */
    private String name;

    /**
     * 二进制表示 (6位, 从下到上)
     */
    private String symbol;

    /**
     * Unicode 符号 (如: ☰☰)
     */
    private String unicodeSymbol;

    /**
     * 上卦
     */
    private String upperTrigram;

    /**
     * 下卦
     */
    private String lowerTrigram;

    /**
     * 完整卦名
     */
    private String fullName;

    /**
     * 卦辞原文
     */
    private String statement;

    /**
     * 卦辞解释
     */
    private String interpretation;

    /**
     * 象征意义
     */
    private String symbolism;

    /**
     * 建议指导
     */
    private String advice;

    /**
     * 人生启示/简短释义
     */
    private String meaning;

    /**
     * 六爻爻辞列表 (从下到上排列)
     */
    private List<LineTextDTO> lineTexts;

    /**
     * 爻辞 DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LineTextDTO {
        /**
         * 爻位 (1-6, 从下到上)
         */
        private Integer position;

        /**
         * 爻名 (如: 初九、六二)
         */
        private String lineName;

        /**
         * 是否为阳爻
         */
        private Boolean isYang;

        /**
         * 爻辞原文
         */
        private String originalText;

        /**
         * 爻辞译文 (可选)
         */
        private String translation;

        /**
         * 爻义 (可选)
         */
        private String meaning;
    }
}

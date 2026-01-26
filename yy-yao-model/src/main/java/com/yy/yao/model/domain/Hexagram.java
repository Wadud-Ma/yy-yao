package com.yy.yao.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 卦象模型 - 代表易经64卦中的一卦
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hexagram {
    /**
     * 卦序号 (1-64)
     */
    private int number;

    /**
     * 卦名
     */
    private String name;

    /**
     * 卦的符号表示 (例如: ䷀)
     */
    private String symbol;

    /**
     * 卦的二进制表示 (从下到上, 0=阴爻, 1=阳爻)
     * 例如: 111111 代表乾卦
     */
    private String binaryCode;

    /**
     * 上卦
     */
    private String upperTrigram;

    /**
     * 下卦
     */
    private String lowerTrigram;

    /**
     * 卦辞
     */
    private String statement;

    /**
     * 卦辞解释
     */
    private String interpretation;

    /**
     * 卦象象征（小程序专用）
     * 例如: "天、创造、刚健、阳气、领导力、积极进取"
     */
    private String symbolism;

    /**
     * 行事建议（小程序专用）
     * 例如: "此时正是发奋进取的好时机，但需警惕过度自信。"
     */
    private String advice;

    /**
     * 六爻爻辞
     */
    private List<LineStatement> lineStatements;

    /**
     * 爻辞模型
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LineStatement {
        /**
         * 爻位 (1-6, 从下到上)
         */
        private int position;

        /**
         * 爻名 (如: 初九, 六二)
         */
        private String name;

        /**
         * 爻辞
         */
        private String text;

        /**
         * 爻辞解释
         */
        private String interpretation;
    }
}

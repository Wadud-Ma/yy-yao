package com.yy.yao.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小程序卦象响应 DTO
 * 简化的卦象数据结构，适配小程序前端
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiniProgramHexagramDTO {
    /**
     * 卦序号 (1-64)
     */
    private Integer id;

    /**
     * 卦名
     */
    private String name;

    /**
     * 卦的二进制表示 (从下到上, 0=阴爻, 1=阳爻)
     * 例如: "111111" 代表乾卦
     */
    private String symbol;

    /**
     * 卦辞
     */
    private String statement;

    /**
     * 卦象象征
     * 例如: "天、创造、刚健、阳气、领导力、积极进取"
     */
    private String symbolism;

    /**
     * 卦辞解释
     */
    private String interpretation;

    /**
     * 行事建议
     */
    private String advice;

    /**
     * 完整卦名（可选，用于显示）
     * 例如: "乾为天"
     */
    private String fullName;

    /**
     * 卦的 Unicode 符号表示（可选）
     * 例如: "☰☰" 或 "䷀"
     */
    private String unicodeSymbol;
}

package com.yy.yao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 小程序占卜响应 DTO
 * 简化的占卜结果，适配小程序前端
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiniProgramDivinationResponse {
    /**
     * 占卜时间戳
     */
    private String timestamp;

    /**
     * 用户问题
     */
    private String question;

    /**
     * 本卦（原始卦象）
     */
    private MiniProgramHexagramDTO hexagram;

    /**
     * 变卦（如果有变爻）
     */
    private MiniProgramHexagramDTO changedHexagram;

    /**
     * 变爻位置列表（0-5，从下到上）
     */
    private List<Integer> changingLines;

    /**
     * 起卦方式
     * SHAKE: 摇卦
     * PLUM: 梅花易数
     * MANUAL: 手动起卦
     * TIME: 时间起卦
     */
    private String method;

    /**
     * 传统解读
     */
    private String interpretation;

    /**
     * AI 智能解读
     */
    private String aiInterpretation;

    /**
     * 占卜方法对应的中文名称
     */
    public String getMethodName() {
        if (method == null) return "未知";
        switch (method.toUpperCase()) {
            case "SHAKE":
                return "摇卦";
            case "PLUM":
                return "梅花易数";
            case "MANUAL":
                return "手动起卦";
            case "TIME":
                return "时间起卦";
            default:
                return method;
        }
    }
}

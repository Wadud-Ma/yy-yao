package com.yy.yao.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 卜卦请求模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DivinationRequest {
    /**
     * 问题或所求之事
     */
    @NotBlank(message = "问题不能为空")
    private String question;

    /**
     * 卜卦方法
     */
    private DivinationMethod method = DivinationMethod.COIN;

    /**
     * 是否需要AI解读
     */
    private boolean needAiInterpretation = true;

    /**
     * 自定义卦象 (可选, 用于手动输入已知的卦象)
     * 格式: 6位数字, 每位为6/7/8/9, 从下到上
     */
    private String customLines;
}

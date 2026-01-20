package com.yy.yao.service;

import com.yy.yao.model.Hexagram;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 解卦服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InterpretationService {

    private final LlmService llmService;

    /**
     * 生成基础解卦
     */
    public String generateBasicInterpretation(Hexagram originalHexagram,
                                              Hexagram changedHexagram,
                                              List<Integer> changingLines) {
        StringBuilder interpretation = new StringBuilder();

        interpretation.append("【本卦】").append(originalHexagram.getName()).append("卦\n");
        interpretation.append(originalHexagram.getStatement()).append("\n");
        interpretation.append("解释: ").append(originalHexagram.getInterpretation()).append("\n\n");

        if (!changingLines.isEmpty()) {
            interpretation.append("【动爻】\n");
            for (Integer position : changingLines) {
                Hexagram.LineStatement lineStatement = originalHexagram.getLineStatements().get(position - 1);
                interpretation.append(lineStatement.getName()).append(": ")
                        .append(lineStatement.getText()).append("\n")
                        .append("解释: ").append(lineStatement.getInterpretation()).append("\n");
            }
            interpretation.append("\n");

            if (changedHexagram != null) {
                interpretation.append("【变卦】").append(changedHexagram.getName()).append("卦\n");
                interpretation.append(changedHexagram.getStatement()).append("\n");
                interpretation.append("解释: ").append(changedHexagram.getInterpretation()).append("\n");
            }
        } else {
            interpretation.append("【说明】无动爻,以本卦卦辞为准。\n");
        }

        return interpretation.toString();
    }

    /**
     * 使用AI生成智能解读
     */
    public String generateAiInterpretation(String question,
                                           Hexagram originalHexagram,
                                           Hexagram changedHexagram,
                                           List<Integer> changingLines) {
        if (!llmService.isAvailable()) {
            log.warn("AI解读功能不可用 - LLM API Key 未配置");
            return "AI解读功能不可用。请参考传统卦辞和爻辞进行解读。\n\n" +
                   "提示: 请在 application.properties 中配置 llm.api.key 以启用AI功能。";
        }

        String promptText = buildAiPrompt(question, originalHexagram, changedHexagram, changingLines);
        try {
            String systemPrompt = "你是一位精通易经的大师，请根据卜卦结果为问卦者提供深入的解读和建议。";
            return llmService.chat(systemPrompt, promptText);
        } catch (Exception e) {
            log.error("AI解读失败", e);
            return "AI解读服务暂时不可用: " + e.getMessage() + "\n\n请参考传统卦辞和爻辞进行解读。";
        }
    }

    /**
     * 构建AI提示词
     */
    private String buildAiPrompt(String question,
                                 Hexagram originalHexagram,
                                 Hexagram changedHexagram,
                                 List<Integer> changingLines) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("【问题】\n").append(question).append("\n\n");

        prompt.append("【本卦】").append(originalHexagram.getName()).append("卦\n");
        prompt.append("卦辞: ").append(originalHexagram.getStatement()).append("\n");
        prompt.append("解释: ").append(originalHexagram.getInterpretation()).append("\n\n");

        if (!changingLines.isEmpty()) {
            prompt.append("【动爻】\n");
            String changingLinesStr = changingLines.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            prompt.append("第 ").append(changingLinesStr).append(" 爻为动爻\n");

            for (Integer position : changingLines) {
                Hexagram.LineStatement lineStatement = originalHexagram.getLineStatements().get(position - 1);
                prompt.append(lineStatement.getName()).append(": ")
                        .append(lineStatement.getText()).append("\n");
            }
            prompt.append("\n");

            if (changedHexagram != null) {
                prompt.append("【变卦】").append(changedHexagram.getName()).append("卦\n");
                prompt.append("卦辞: ").append(changedHexagram.getStatement()).append("\n");
                prompt.append("解释: ").append(changedHexagram.getInterpretation()).append("\n\n");
            }
        }

        prompt.append("请从以下几个方面进行解读:\n");
        prompt.append("1. 总体运势: 结合本卦和变卦,分析当前的整体形势\n");
        prompt.append("2. 具体建议: 针对问题,提供具体可行的行动建议\n");
        prompt.append("3. 注意事项: 指出需要警惕的方面\n");
        prompt.append("4. 时机把握: 说明行动的最佳时机\n\n");

        prompt.append("请用通俗易懂的语言,避免过于晦涩的术语,让问卦者能够理解并应用到实际生活中。");

        return prompt.toString();
    }

    /**
     * 生成简化版AI解读
     */
    public String generateSimpleAiInterpretation(String question, String hexagramName) {
        if (!llmService.isAvailable()) {
            return "当前情况建议参考" + hexagramName + "卦的卦辞和爻辞,谨慎行事。";
        }

        String promptText = String.format(
                "用户提问: %s\n得到了易经的%s卦。\n请用2-3句话简单解读这个卦象对用户问题的启示。",
                question, hexagramName
        );
        try {
            String systemPrompt = "你是一位易经专家，请用简洁的语言解读卦象。";
            return llmService.chat(systemPrompt, promptText);
        } catch (Exception e) {
            log.error("简化AI解读失败", e);
            return "当前情况建议参考" + hexagramName + "卦的卦辞和爻辞,谨慎行事。";
        }
    }
}

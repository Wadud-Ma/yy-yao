package com.yy.yao.service.mapper;

import com.yy.yao.model.dto.DivinationResponse;
import com.yy.yao.model.dto.MiniProgramDivinationResponse;
import com.yy.yao.model.dto.MiniProgramHexagramDTO;
import com.yy.yao.model.domain.Hexagram;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * 小程序数据映射工具
 * 将后端数据模型转换为小程序需要的格式
 */
@Component
public class MiniProgramMapper {

    /**
     * 将 Hexagram 转换为 MiniProgramHexagramDTO
     */
    public MiniProgramHexagramDTO toMiniProgramHexagram(Hexagram hexagram) {
        if (hexagram == null) {
            return null;
        }

        MiniProgramHexagramDTO dto = new MiniProgramHexagramDTO();
        dto.setId(hexagram.getNumber());
        dto.setName(hexagram.getName());
        dto.setSymbol(hexagram.getBinaryCode());
        dto.setStatement(hexagram.getStatement());
        dto.setSymbolism(hexagram.getSymbolism());
        dto.setInterpretation(hexagram.getInterpretation());
        dto.setAdvice(hexagram.getAdvice());
        dto.setUnicodeSymbol(hexagram.getSymbol());

        // 生成完整卦名
        dto.setFullName(generateFullName(hexagram));

        return dto;
    }

    /**
     * 将 DivinationResponse 转换为 MiniProgramDivinationResponse
     */
    public MiniProgramDivinationResponse toMiniProgramResponse(DivinationResponse response) {
        if (response == null) {
            return null;
        }

        MiniProgramDivinationResponse dto = new MiniProgramDivinationResponse();
        // 转换 LocalDateTime 为 ISO 字符串
        dto.setTimestamp(response.getTimestamp() != null ? response.getTimestamp().toString() : null);
        dto.setQuestion(response.getQuestion());
        dto.setHexagram(toMiniProgramHexagram(response.getOriginalHexagram()));
        dto.setChangedHexagram(toMiniProgramHexagram(response.getChangedHexagram()));
        dto.setMethod(response.getMethod() != null ? response.getMethod().name() : null);
        dto.setInterpretation(response.getInterpretation());
        dto.setAiInterpretation(response.getAiInterpretation());

        // 转换变爻位置（后端是1-6，小程序是0-5）
        if (response.getChangingLines() != null) {
            dto.setChangingLines(
                response.getChangingLines().stream()
                    .map(line -> line - 1) // 转换为0-5索引
                    .collect(Collectors.toList())
            );
        }

        return dto;
    }

    /**
     * 生成完整卦名
     * 例如: "乾为天"、"坤为地"、"水雷屯"
     */
    private String generateFullName(Hexagram hexagram) {
        if (hexagram == null) {
            return "未知卦";
        }

        String name = hexagram.getName();
        if (name == null || name.isEmpty()) {
            return "未知卦";
        }

        String upper = hexagram.getUpperTrigram();
        String lower = hexagram.getLowerTrigram();

        // 如果上下卦相同
        if (upper != null && !upper.isEmpty() && upper.equals(lower)) {
            String element = getTrigramElement(upper);
            return name + "为" + element;
        }

        // 如果上下卦不同，组合卦名
        if (upper != null && !upper.isEmpty() && lower != null && !lower.isEmpty()) {
            String upperElement = getTrigramElement(upper);
            String lowerElement = getTrigramElement(lower);
            return upperElement + lowerElement + name;
        }

        // 如果没有上下卦信息，只返回卦名
        return name;
    }

    /**
     * 获取卦的五行属性
     */
    private String getTrigramElement(String trigram) {
        if (trigram == null) return "";

        return switch (trigram) {
            case "乾" -> "天";
            case "坤" -> "地";
            case "震" -> "雷";
            case "巽" -> "风";
            case "坎" -> "水";
            case "离" -> "火";
            case "艮" -> "山";
            case "兑" -> "泽";
            default -> trigram;
        };
    }
}

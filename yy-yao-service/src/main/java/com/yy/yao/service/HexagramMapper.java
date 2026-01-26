package com.yy.yao.service;

import com.yy.yao.entity.HexagramLine;
import com.yy.yao.repository.HexagramLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 卦象实体与模型转换器
 * 负责在 Entity 层 (com.yy.yao.entity.Hexagram) 和 Model 层 (com.yy.yao.model.Hexagram) 之间转换
 * 使用 Java 21 特性提升性能和代码质量
 */
@Component
@RequiredArgsConstructor
public class HexagramMapper {

    private final HexagramLineRepository hexagramLineRepository;

    /**
     * Entity → Model 转换
     * @param entity 数据库实体
     * @return 业务模型
     */
    public com.yy.yao.model.Hexagram toModel(com.yy.yao.entity.Hexagram entity) {
        if (entity == null) {
            return null;
        }

        var model = new com.yy.yao.model.Hexagram();
        model.setNumber(entity.getId());
        model.setName(entity.getName());
        model.setSymbol(entity.getUnicodeSymbol());
        model.setBinaryCode(entity.getSymbol());

        // 从 Trigram 表获取上下卦名称（如需要，这里简化处理）
        model.setUpperTrigram(getTrigramName(entity.getUpperTrigramId()));
        model.setLowerTrigram(getTrigramName(entity.getLowerTrigramId()));

        // 卦辞和解释
        model.setStatement(entity.getJudgment());
        model.setInterpretation(entity.getJudgmentTranslation());
        model.setSymbolism(entity.getSymbolism());
        model.setAdvice(entity.getAdvice());

        // 加载爻辞
        List<HexagramLine> lines = hexagramLineRepository.findByHexagramIdOrderByPosition(entity.getId());
        model.setLineStatements(toLineStatements(lines));

        return model;
    }

    /**
     * Entity List → Model List 批量转换
     * 使用 Java 21 的 Stream API
     */
    public List<com.yy.yao.model.Hexagram> toModelList(List<com.yy.yao.entity.Hexagram> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .map(this::toModel)
                .toList();
    }

    /**
     * HexagramLine Entity → LineStatement Model
     */
    private List<com.yy.yao.model.Hexagram.LineStatement> toLineStatements(List<HexagramLine> lines) {
        if (lines == null || lines.isEmpty()) {
            return List.of();
        }

        return lines.stream()
                .map(line -> {
                    var statement = new com.yy.yao.model.Hexagram.LineStatement();
                    statement.setPosition(line.getPosition());
                    statement.setName(line.getName());
                    statement.setText(line.getLineText());
                    statement.setInterpretation(line.getLineTranslation());
                    return statement;
                })
                .toList();
    }

    /**
     * 简化版：根据 Trigram ID 获取卦名
     * 实际项目中可以注入 TrigramRepository
     */
    private String getTrigramName(Integer trigramId) {
        if (trigramId == null) {
            return "";
        }

        return switch (trigramId) {
            case 1 -> "乾";
            case 2 -> "兑";
            case 3 -> "离";
            case 4 -> "震";
            case 5 -> "巽";
            case 6 -> "坎";
            case 7 -> "艮";
            case 8 -> "坤";
            default -> "";
        };
    }
}

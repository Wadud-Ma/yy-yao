package com.yy.yao.service;

import com.yy.yao.dto.HexagramWithLinesDTO;
import com.yy.yao.entity.Hexagram;
import com.yy.yao.entity.HexagramLine;
import com.yy.yao.repository.HexagramLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 卦象实体到 DTO 的映射服务
 */
@Service
@RequiredArgsConstructor
public class HexagramDTOMapper {

    private final HexagramLineRepository hexagramLineRepository;

    /**
     * 转换为包含爻辞的完整 DTO
     */
    public HexagramWithLinesDTO toHexagramWithLinesDTO(Hexagram hexagram) {
        if (hexagram == null) {
            return null;
        }

        // 查询爻辞数据
        List<HexagramLine> hexagramLines = hexagramLineRepository
            .findByHexagramIdOrderByPosition(hexagram.getId());

        // 转换爻辞为 DTO
        List<HexagramWithLinesDTO.LineTextDTO> lineTextDTOs = hexagramLines.stream()
            .map(this::toLineTextDTO)
            .collect(Collectors.toList());

        // 构建完整 DTO
        return HexagramWithLinesDTO.builder()
            .id(hexagram.getId())
            .name(hexagram.getName())
            .symbol(hexagram.getSymbol())
            .unicodeSymbol(hexagram.getUnicodeSymbol())
            .fullName(hexagram.getFullName())
            .statement(hexagram.getJudgment())
            .interpretation(hexagram.getJudgmentTranslation())
            .symbolism(hexagram.getSymbolism())
            .advice(hexagram.getAdvice())
            .meaning(hexagram.getLifeLesson())
            .lineTexts(lineTextDTOs)
            .build();
    }

    /**
     * 转换爻辞实体为 DTO
     */
    private HexagramWithLinesDTO.LineTextDTO toLineTextDTO(HexagramLine hexagramLine) {
        return HexagramWithLinesDTO.LineTextDTO.builder()
            .position(hexagramLine.getPosition())
            .lineName(hexagramLine.getName())
            .isYang(hexagramLine.getType() == HexagramLine.LineType.yang)
            .originalText(hexagramLine.getLineText())
            .translation(hexagramLine.getLineTranslation())
            .meaning(hexagramLine.getLineAnnotation())
            .build();
    }

    /**
     * 批量转换为包含爻辞的完整 DTO
     */
    public List<HexagramWithLinesDTO> toHexagramWithLinesDTOList(List<Hexagram> hexagrams) {
        return hexagrams.stream()
            .map(this::toHexagramWithLinesDTO)
            .collect(Collectors.toList());
    }
}

package com.yy.yao.service;

import com.yy.yao.model.DivinationRequest;
import com.yy.yao.model.DivinationResult;
import com.yy.yao.model.Hexagram;
import com.yy.yao.repository.HexagramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 卜卦服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DivinationService {

    private final DivinationAlgorithm algorithm;
    private final HexagramRepository hexagramRepository;
    private final InterpretationService interpretationService;

    /**
     * 执行卜卦
     */
    public DivinationResult performDivination(DivinationRequest request) {
        log.info("开始卜卦, 问题: {}, 方法: {}", request.getQuestion(), request.getMethod());

        // 1. 生成六爻
        List<DivinationResult.LineResult> lines = generateLines(request);

        // 2. 计算本卦
        String originalBinary = algorithm.linesToBinary(lines);
        Hexagram originalHexagram = hexagramRepository.findByBinary(originalBinary)
                .orElseThrow(() -> new RuntimeException("未找到对应的卦象: " + originalBinary));

        // 3. 计算动爻和变卦
        List<Integer> changingLines = algorithm.getChangingLinePositions(lines);
        Hexagram changedHexagram = null;

        if (!changingLines.isEmpty()) {
            String changedBinary = algorithm.calculateChangedBinary(lines);
            changedHexagram = hexagramRepository.findByBinary(changedBinary).orElse(null);
        }

        // 4. 生成解卦
        String interpretation = interpretationService.generateBasicInterpretation(
                originalHexagram, changedHexagram, changingLines);

        // 5. AI智能解读
        String aiInterpretation = null;
        if (request.isNeedAiInterpretation()) {
            try {
                aiInterpretation = interpretationService.generateAiInterpretation(
                        request.getQuestion(), originalHexagram, changedHexagram, changingLines);
            } catch (Exception e) {
                log.warn("AI解读失败: {}", e.getMessage());
                aiInterpretation = "AI解读暂时不可用";
            }
        }

        // 6. 构建结果
        return DivinationResult.builder()
                .timestamp(LocalDateTime.now())
                .question(request.getQuestion())
                .originalHexagram(originalHexagram)
                .changedHexagram(changedHexagram)
                .changingLines(changingLines)
                .lines(lines)
                .method(request.getMethod())
                .interpretation(interpretation)
                .aiInterpretation(aiInterpretation)
                .build();
    }

    /**
     * 根据方法生成六爻
     */
    private List<DivinationResult.LineResult> generateLines(DivinationRequest request) {
        // 如果有自定义卦象,使用自定义方法
        if (request.getCustomLines() != null && !request.getCustomLines().isEmpty()) {
            return algorithm.customMethod(request.getCustomLines());
        }

        // 根据卜卦方法生成
        return switch (request.getMethod()) {
            case COIN -> algorithm.coinMethod();
            case YARROW -> algorithm.yarrowMethod();
            case NUMBER -> algorithm.numberMethod();
            case TIME -> algorithm.timeMethod();
        };
    }

    /**
     * 查询卦象
     */
    public Optional<Hexagram> getHexagramByNumber(int number) {
        return hexagramRepository.findByNumber(number);
    }

    /**
     * 获取所有卦象
     */
    public List<Hexagram> getAllHexagrams() {
        return hexagramRepository.findAll();
    }
}

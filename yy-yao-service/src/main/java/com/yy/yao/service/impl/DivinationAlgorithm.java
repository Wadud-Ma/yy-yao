package com.yy.yao.service.impl;

import com.yy.yao.model.domain.DivinationResult;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * 卜卦算法服务
 */
@Service
public class DivinationAlgorithm {

    private final SecureRandom random = new SecureRandom();

    /**
     * 铜钱法起卦
     * 传统方法: 用3枚铜钱,投掷6次
     * 规则: 3个正面=老阳(9,动), 3个反面=老阴(6,动), 2正1反=少阳(7), 2反1正=少阴(8)
     */
    public List<DivinationResult.LineResult> coinMethod() {
        List<DivinationResult.LineResult> lines = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            int heads = 0;
            // 投掷3枚铜钱
            for (int j = 0; j < 3; j++) {
                if (random.nextBoolean()) {
                    heads++;
                }
            }

            DivinationResult.LineResult line = createLineResult(i, heads);
            lines.add(line);
        }

        return lines;
    }

    /**
     * 根据铜钱结果创建爻
     */
    private DivinationResult.LineResult createLineResult(int position, int heads) {
        int value;
        boolean yang;
        boolean changing;

        switch (heads) {
            case 3: // 3个正面 = 老阳(动)
                value = 9;
                yang = true;
                changing = true;
                break;
            case 2: // 2个正面 = 少阳
                value = 7;
                yang = true;
                changing = false;
                break;
            case 1: // 1个正面 = 少阴
                value = 8;
                yang = false;
                changing = false;
                break;
            default: // 0个正面 = 老阴(动)
                value = 6;
                yang = false;
                changing = true;
                break;
        }

        return new DivinationResult.LineResult(position, yang, changing, value);
    }

    /**
     * 数字法起卦
     * 现代简化方法: 直接随机生成阴阳爻
     */
    public List<DivinationResult.LineResult> numberMethod() {
        List<DivinationResult.LineResult> lines = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            // 随机决定阴阳
            boolean yang = random.nextBoolean();
            // 10%概率为动爻
            boolean changing = random.nextInt(10) == 0;

            int value;
            if (yang) {
                value = changing ? 9 : 7;
            } else {
                value = changing ? 6 : 8;
            }

            lines.add(new DivinationResult.LineResult(i, yang, changing, value));
        }

        return lines;
    }

    /**
     * 时间起卦法
     * 根据当前时间生成卦象
     */
    public List<DivinationResult.LineResult> timeMethod() {
        long timestamp = System.currentTimeMillis();

        List<DivinationResult.LineResult> lines = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            // 使用时间戳的不同位来生成卦象
            int seed = (int) ((timestamp >> (i * 4)) & 0xFF);
            boolean yang = (seed % 2) == 1;
            boolean changing = (seed % 10) == 0;

            int value;
            if (yang) {
                value = changing ? 9 : 7;
            } else {
                value = changing ? 6 : 8;
            }

            lines.add(new DivinationResult.LineResult(i, yang, changing, value));
        }

        return lines;
    }

    /**
     * 蓍草法起卦 (传统方法,较复杂)
     * 这里实现简化版本
     */
    public List<DivinationResult.LineResult> yarrowMethod() {
        List<DivinationResult.LineResult> lines = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            // 简化的蓍草法: 模拟三次揲蓍过程
            int sum = 0;
            for (int j = 0; j < 3; j++) {
                sum += simulateYarrowDivision();
            }

            // 根据总和判断爻的性质
            // 6=老阴, 7=少阳, 8=少阴, 9=老阳
            boolean yang = (sum == 7 || sum == 9);
            boolean changing = (sum == 6 || sum == 9);

            lines.add(new DivinationResult.LineResult(i, yang, changing, sum));
        }

        return lines;
    }

    /**
     * 模拟一次揲蓍过程
     */
    private int simulateYarrowDivision() {
        // 简化算法: 随机返回2或3
        // 在真实的蓍草法中,这个过程更复杂
        int result = random.nextInt(4);
        return result == 0 ? 2 : 3;
    }

    /**
     * 根据自定义爻值字符串创建卦象
     * @param customLines 6位数字字符串,每位为6/7/8/9
     */
    public List<DivinationResult.LineResult> customMethod(String customLines) {
        if (customLines == null || customLines.length() != 6) {
            throw new IllegalArgumentException("自定义爻值必须是6位数字");
        }

        List<DivinationResult.LineResult> lines = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            char c = customLines.charAt(i);
            int value = Character.getNumericValue(c);

            if (value < 6 || value > 9) {
                throw new IllegalArgumentException("每位爻值必须在6-9之间");
            }

            boolean yang = (value == 7 || value == 9);
            boolean changing = (value == 6 || value == 9);

            lines.add(new DivinationResult.LineResult(i + 1, yang, changing, value));
        }

        return lines;
    }

    /**
     * 将爻结果列表转换为二进制字符串
     */
    public String linesToBinary(List<DivinationResult.LineResult> lines) {
        StringBuilder binary = new StringBuilder();
        for (DivinationResult.LineResult line : lines) {
            binary.append(line.isYang() ? "1" : "0");
        }
        return binary.toString();
    }

    /**
     * 计算变卦的二进制表示
     */
    public String calculateChangedBinary(List<DivinationResult.LineResult> lines) {
        StringBuilder binary = new StringBuilder();
        for (DivinationResult.LineResult line : lines) {
            if (line.isChanging()) {
                // 动爻变化: 阳变阴,阴变阳
                binary.append(line.isYang() ? "0" : "1");
            } else {
                binary.append(line.isYang() ? "1" : "0");
            }
        }
        return binary.toString();
    }

    /**
     * 获取动爻位置列表
     */
    public List<Integer> getChangingLinePositions(List<DivinationResult.LineResult> lines) {
        List<Integer> positions = new ArrayList<>();
        for (DivinationResult.LineResult line : lines) {
            if (line.isChanging()) {
                positions.add(line.getPosition());
            }
        }
        return positions;
    }
}

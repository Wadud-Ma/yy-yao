package com.yy.yao.model.domain;

/**
 * 占卜方式枚举
 * 统一的枚举定义，供 Entity 层和 Model 层共用
 * 使用 Java 21 特性
 */
public enum DivinationMethod {
    /**
     * 铜钱法（三枚铜钱摇卦）
     */
    COIN("铜钱法"),

    /**
     * 手工起卦（蓍草或数字）
     */
    MANUAL("手工起卦"),

    /**
     * 梅花易数（时间、方位等）
     */
    MEIHUA("梅花易数"),

    /**
     * 每日一卦
     */
    DAILY("每日一卦"),

    /**
     * 数字法
     */
    NUMBER("数字法"),

    /**
     * 时间起卦法
     */
    TIME("时间起卦法"),

    /**
     * 蓍草法
     */
    YARROW("蓍草法"),

    /**
     * 抽签
     */
    QIAN("抽签");

    private final String description;

    DivinationMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

package com.yy.yao.service.impl;

import com.yy.yao.dao.repository.HexagramLineRepository;
import com.yy.yao.dao.repository.HexagramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * 卦象数据初始化服务
 * 在应用启动时检查并初始化64卦数据
 * 使用 Java 21 特性优化代码
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Order(1)
public class HexagramDataInitService implements CommandLineRunner {

    private final HexagramRepository hexagramRepository;
    private final HexagramLineRepository hexagramLineRepository;

    @Override
    public void run(String... args) {
        log.info("=".repeat(60));
        log.info("检查卦象数据初始化状态...");
        log.info("=".repeat(60));

        checkAndReportDataStatus();
    }

    /**
     * 检查并报告数据状态
     */
    public void checkAndReportDataStatus() {
        long hexagramCount = hexagramRepository.count();
        long lineTextCount = hexagramLineRepository.count();

        log.info("当前数据状态:");
        log.info("   - 卦象数量: {} / 64", hexagramCount);
        log.info("   - 爻辞数量: {} / 384 (64卦 × 6爻)", lineTextCount);

        if (hexagramCount == 0 || lineTextCount == 0) {
            logInitializationInstructions();
        } else if (hexagramCount < 64 || lineTextCount < 384) {
            logIncompleteDataWarning(hexagramCount, lineTextCount);
        } else {
            logDataCompleteInfo();
        }

        log.info("=".repeat(60));
    }

    /**
     * 输出数据初始化指令
     */
    private void logInitializationInstructions() {
        log.warn("数据库未初始化!");
        log.warn("");
        log.warn("请执行以下 SQL 脚本初始化数据:");
        log.warn("   文件: docs/hexagrams-full-data-init.sql");
        log.warn("");
        log.warn("执行方式:");
        log.warn("   1. MySQL 命令行:");
        log.warn("      mysql -u root -p yy_yao < docs/hexagrams-full-data-init.sql");
        log.warn("");
        log.warn("   2. MySQL Workbench:");
        log.warn("      - 打开 SQL 文件");
        log.warn("      - 点击执行");
        log.warn("");
        log.warn("   3. 使用 Spring Boot:");
        log.warn("      - 将 SQL 文件放到 src/main/resources/data.sql");
        log.warn("      - 配置 spring.sql.init.mode=always");
        log.warn("");
    }

    /**
     * 输出数据不完整警告
     */
    private void logIncompleteDataWarning(long hexagramCount, long lineTextCount) {
        log.warn("数据不完整!");
        log.warn("   缺少 {} 个卦象", 64 - hexagramCount);
        log.warn("   缺少 {} 条爻辞", 384 - lineTextCount);
        log.warn("   建议重新导入 SQL 脚本");
    }

    /**
     * 输出数据完整信息和示例
     */
    private void logDataCompleteInfo() {
        log.info("数据完整, 系统就绪!");

        // 使用 Java 21 的 ifPresent 简洁处理
        hexagramRepository.findById(1).ifPresent(hex -> {
            log.info("");
            log.info("示例数据 - 第1卦:");
            log.info("   卦名: {}", hex.getName());
            log.info("   卦符: {} ({})", hex.getUnicodeSymbol(), hex.getSymbol());

            var judgment = hex.getJudgment();
            if (judgment != null) {
                var preview = judgment.length() > 50
                    ? judgment.substring(0, 50) + "..."
                    : judgment;
                log.info("   卦辞: {}", preview);
            }
        });
    }
}

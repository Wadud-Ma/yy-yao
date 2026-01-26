package com.yy.yao.biz;

import com.yy.yao.model.DivinationRequest;
import com.yy.yao.model.DivinationResult;
import com.yy.yao.service.DivinationRecordService;
import com.yy.yao.service.DivinationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 占卜业务编排服务
 * 负责组合多个 Service 完成完整的占卜业务流程
 *
 * 职责:
 * 1. 组合 DivinationService 执行占卜
 * 2. 保存占卜记录到数据库
 * 3. 处理事务边界
 * 4. 实现业务补偿逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DivinationBizService {

    private final DivinationService divinationService;
    private final DivinationRecordService divinationRecordService;

    /**
     * 执行完整的占卜流程
     * 包括: 占卦 → 保存记录
     *
     * @param userId 用户ID (可选,为null时不保存记录)
     * @param request 占卜请求
     * @return 占卜结果
     */
    @Transactional
    public DivinationResult performCompleteDivination(Long userId, DivinationRequest request) {
        log.info("开始执行占卜业务流程 - 用户ID: {}, 问题: {}", userId, request.getQuestion());

        try {
            // 1. 执行占卜
            DivinationResult result = divinationService.performDivination(request);
            log.info("占卜完成 - 本卦: {}, 变卦: {}",
                    result.getOriginalHexagram().getName(),
                    result.getChangedHexagram() != null ? result.getChangedHexagram().getName() : "无");

            // 2. 保存占卜记录 (如果提供了用户ID)
            if (userId != null) {
                try {
                    divinationRecordService.saveDivinationRecord(userId, result);
                    log.info("占卜记录已保存 - 用户ID: {}", userId);
                } catch (Exception e) {
                    log.error("保存占卜记录失败,但不影响占卜结果返回", e);
                    // 不抛出异常,保证占卜结果可以正常返回
                }
            }

            return result;

        } catch (Exception e) {
            log.error("执行占卜业务流程失败", e);
            throw new RuntimeException("占卜失败: " + e.getMessage(), e);
        }
    }

    /**
     * 执行匿名占卜 (不保存记录)
     *
     * @param request 占卜请求
     * @return 占卜结果
     */
    public DivinationResult performAnonymousDivination(DivinationRequest request) {
        return performCompleteDivination(null, request);
    }
}

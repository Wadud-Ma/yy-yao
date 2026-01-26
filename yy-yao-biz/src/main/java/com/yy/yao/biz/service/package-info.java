/**
 * 业务编排层 (Business Orchestration Layer)
 *
 * <h2>职责</h2>
 * <ul>
 *   <li>组合多个 Service 实现复杂业务流程</li>
 *   <li>处理事务边界</li>
 *   <li>实现业务补偿逻辑</li>
 *   <li>协调跨服务的业务操作</li>
 * </ul>
 *
 * <h2>设计原则</h2>
 * <ul>
 *   <li>✓ 编排多个 Service 完成复杂业务</li>
 *   <li>✓ 处理事务边界</li>
 *   <li>✓ 实现业务补偿逻辑</li>
 *   <li>✗ 不直接访问 DAO 层</li>
 *   <li>✗ 不处理 HTTP 请求</li>
 * </ul>
 *
 * <h2>示例</h2>
 * <pre>
 * // DivinationBizService 组合多个 Service
 * public DivinationResult performCompleteDivination(Long userId, DivinationRequest request) {
 *     // 1. 执行占卜
 *     var result = divinationService.performDivination(request);
 *
 *     // 2. 保存记录
 *     if (userId != null) {
 *         divinationRecordService.saveDivinationRecord(userId, result);
 *     }
 *
 *     // 3. 返回结果
 *     return result;
 * }
 * </pre>
 *
 * @since 0.0.1
 */
package com.yy.yao.biz.service;

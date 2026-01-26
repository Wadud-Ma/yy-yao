package com.yy.yao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yy.yao.entity.DivinationRecord;
import com.yy.yao.model.DivinationResult;
import com.yy.yao.model.Hexagram;
import com.yy.yao.repository.DivinationRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

/**
 * 占卜记录服务
 * 使用 Java 21 新特性优化代码
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DivinationRecordService {

    private final DivinationRecordRepository recordRepository;
    private final ObjectMapper objectMapper;

    /**
     * 保存占卜记录
     * @param userId 用户ID
     * @param result 占卜结果
     */
    @Transactional
    public DivinationRecord saveDivinationRecord(Long userId, DivinationResult result) {
        var record = DivinationRecord.builder()
                .userId(userId)
                .question(result.getQuestion())
                .method(result.getMethod())
                .divinationTime(result.getTimestamp())
                .interpretation(result.getInterpretation())
                .aiInterpretation(result.getAiInterpretation())
                .usedAi(result.getAiInterpretation() != null && !result.getAiInterpretation().isEmpty())
                .isFavorited(false)
                .build();

        // 设置本卦信息
        var originalHexagram = result.getOriginalHexagram();
        if (originalHexagram != null) {
            record.setOriginalHexagramId(originalHexagram.getNumber());
            record.setOriginalHexagramName(originalHexagram.getName());
            record.setOriginalHexagramBinary(originalHexagram.getBinaryCode());
        }

        // 设置变卦信息
        var changedHexagram = result.getChangedHexagram();
        if (changedHexagram != null) {
            record.setChangedHexagramId(changedHexagram.getNumber());
            record.setChangedHexagramName(changedHexagram.getName());
            record.setChangedHexagramBinary(changedHexagram.getBinaryCode());
        }

        // 设置动爻列表（JSON序列化）
        if (result.getChangingLines() != null && !result.getChangingLines().isEmpty()) {
            try {
                record.setChangingLines(objectMapper.writeValueAsString(result.getChangingLines()));
            } catch (JsonProcessingException e) {
                log.error("序列化动爻列表失败", e);
            }
        }

        // 设置六爻详细信息（JSON序列化）
        if (result.getLines() != null && !result.getLines().isEmpty()) {
            try {
                record.setLinesDetail(objectMapper.writeValueAsString(result.getLines()));
            } catch (JsonProcessingException e) {
                log.error("序列化六爻详细信息失败", e);
            }
        }

        return recordRepository.save(record);
    }

    /**
     * 根据ID查找记录
     */
    public Optional<DivinationRecord> findById(Long id) {
        return recordRepository.findById(id);
    }

    /**
     * 查找用户的所有记录
     */
    public Page<DivinationRecord> findUserRecords(Long userId, Pageable pageable) {
        return recordRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    /**
     * 查找用户的收藏记录
     */
    public Page<DivinationRecord> findUserFavorites(Long userId, Pageable pageable) {
        return recordRepository.findByUserIdAndIsFavoritedOrderByCreatedAtDesc(userId, true, pageable);
    }

    /**
     * 查找指定时间范围内的记录
     */
    public Page<DivinationRecord> findByTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        return recordRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, startTime, endTime, pageable);
    }

    /**
     * 统计用户今日占卜次数
     */
    public long countTodayDivinations(Long userId) {
        var todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        return recordRepository.countTodayDivinations(userId, todayStart);
    }

    /**
     * 统计用户总占卜次数
     */
    public long countTotalDivinations(Long userId) {
        return recordRepository.countByUserId(userId);
    }

    /**
     * 切换收藏状态
     */
    @Transactional
    public boolean toggleFavorite(Long recordId, Long userId) {
        return recordRepository.findById(recordId)
                .filter(record -> Objects.equals(record.getUserId(), userId))
                .map(record -> {
                    record.setIsFavorited(!record.getIsFavorited());
                    recordRepository.save(record);
                    return true;
                })
                .orElseGet(() -> {
                    log.warn("记录不存在或用户 {} 无权访问记录 {}", userId, recordId);
                    return false;
                });
    }

    /**
     * 添加备注
     */
    @Transactional
    public boolean addRemark(Long recordId, Long userId, String remark) {
        return recordRepository.findById(recordId)
                .filter(record -> Objects.equals(record.getUserId(), userId))
                .map(record -> {
                    record.setRemark(remark);
                    recordRepository.save(record);
                    return true;
                })
                .orElseGet(() -> {
                    log.warn("记录不存在或用户 {} 无权访问记录 {}", userId, recordId);
                    return false;
                });
    }

    /**
     * 删除记录
     */
    @Transactional
    public boolean deleteRecord(Long recordId, Long userId) {
        return recordRepository.findById(recordId)
                .filter(record -> Objects.equals(record.getUserId(), userId))
                .map(record -> {
                    recordRepository.delete(record);
                    return true;
                })
                .orElseGet(() -> {
                    log.warn("记录不存在或用户 {} 无权访问记录 {}", userId, recordId);
                    return false;
                });
    }

    /**
     * 统计系统总占卜次数
     */
    public long countSystemTotalDivinations() {
        return recordRepository.countTotalDivinations();
    }

    /**
     * 根据卦象ID查找记录
     */
    public Page<DivinationRecord> findByHexagramId(Integer hexagramId, Pageable pageable) {
        return recordRepository.findByOriginalHexagramIdOrderByCreatedAtDesc(hexagramId, pageable);
    }

    /**
     * 清除用户的所有历史记录（保留收藏）
     */
    @Transactional
    public void clearHistory(Long userId) {
        log.info("清除用户历史记录: UserId={}", userId);
        var records = recordRepository.findByUserIdOrderByCreatedAtDesc(userId, Pageable.unpaged()).getContent();

        // 使用 Stream API 只删除未收藏的记录
        records.stream()
                .filter(r -> !r.getIsFavorited())
                .forEach(recordRepository::delete);
    }

    /**
     * 取消用户的所有收藏
     */
    @Transactional
    public void clearFavorites(Long userId) {
        log.info("清除用户收藏记录: UserId={}", userId);
        var records = recordRepository.findByUserIdAndIsFavoritedOrderByCreatedAtDesc(userId, true, Pageable.unpaged()).getContent();

        // 取消收藏标记
        records.forEach(record -> {
            record.setIsFavorited(false);
            recordRepository.save(record);
        });
    }

}

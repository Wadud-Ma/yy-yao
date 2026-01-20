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
import java.util.List;
import java.util.Optional;

/**
 * 占卜记录服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DivinationRecordService {

    private final DivinationRecordRepository recordRepository;
    private final ObjectMapper objectMapper;

    /**
     * 保存占卜记录
     */
    @Transactional
    public DivinationRecord saveDivinationRecord(String openid, DivinationResult result) {
        DivinationRecord record = DivinationRecord.builder()
                .openid(openid)
                .question(result.getQuestion())
                .divinationMethod(result.getMethod())
                .divinationTime(result.getTimestamp())
                .interpretation(result.getInterpretation())
                .aiInterpretation(result.getAiInterpretation())
                .usedAi(result.getAiInterpretation() != null && !result.getAiInterpretation().isEmpty())
                .isFavorite(false)
                .build();

        // 设置本卦信息
        if (result.getOriginalHexagram() != null) {
            Hexagram original = result.getOriginalHexagram();
            record.setOriginalHexagramId(original.getNumber());
            record.setOriginalHexagramName(original.getName());
            record.setOriginalHexagramBinary(original.getBinaryCode());
        }

        // 设置变卦信息
        if (result.getChangedHexagram() != null) {
            Hexagram changed = result.getChangedHexagram();
            record.setChangedHexagramId(changed.getNumber());
            record.setChangedHexagramName(changed.getName());
            record.setChangedHexagramBinary(changed.getBinaryCode());
        }

        // 设置动爻列表
        if (result.getChangingLines() != null && !result.getChangingLines().isEmpty()) {
            try {
                record.setChangingLines(objectMapper.writeValueAsString(result.getChangingLines()));
            } catch (JsonProcessingException e) {
                log.error("序列化动爻列表失败", e);
            }
        }

        // 设置六爻详细信息
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
    public Page<DivinationRecord> findUserRecords(String openid, Pageable pageable) {
        return recordRepository.findByOpenidOrderByCreatedAtDesc(openid, pageable);
    }

    /**
     * 查找用户的收藏记录
     */
    public Page<DivinationRecord> findUserFavorites(String openid, Pageable pageable) {
        return recordRepository.findByOpenidAndIsFavoriteTrueOrderByCreatedAtDesc(openid, pageable);
    }

    /**
     * 查找用户最近的N条记录
     */
    public List<DivinationRecord> findRecentRecords(String openid) {
        return recordRepository.findTop10ByOpenidOrderByCreatedAtDesc(openid);
    }

    /**
     * 查找指定时间范围内的记录
     */
    public List<DivinationRecord> findByTimeRange(String openid, LocalDateTime startTime, LocalDateTime endTime) {
        return recordRepository.findByOpenidAndTimeRange(openid, startTime, endTime);
    }

    /**
     * 统计用户今日占卜次数
     */
    public long countTodayDivinations(String openid) {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        return recordRepository.countTodayDivinations(openid, todayStart);
    }

    /**
     * 统计用户总占卜次数
     */
    public long countTotalDivinations(String openid) {
        return recordRepository.countByOpenid(openid);
    }

    /**
     * 切换收藏状态
     */
    @Transactional
    public boolean toggleFavorite(Long recordId, String openid) {
        Optional<DivinationRecord> recordOpt = recordRepository.findById(recordId);
        if (recordOpt.isEmpty()) {
            return false;
        }

        DivinationRecord record = recordOpt.get();
        // 验证记录归属
        if (!record.getOpenid().equals(openid)) {
            log.warn("用户 {} 尝试收藏不属于自己的记录 {}", openid, recordId);
            return false;
        }

        record.setIsFavorite(!record.getIsFavorite());
        recordRepository.save(record);
        return true;
    }

    /**
     * 添加备注
     */
    @Transactional
    public boolean addRemark(Long recordId, String openid, String remark) {
        Optional<DivinationRecord> recordOpt = recordRepository.findById(recordId);
        if (recordOpt.isEmpty()) {
            return false;
        }

        DivinationRecord record = recordOpt.get();
        // 验证记录归属
        if (!record.getOpenid().equals(openid)) {
            log.warn("用户 {} 尝试修改不属于自己的记录 {}", openid, recordId);
            return false;
        }

        record.setRemark(remark);
        recordRepository.save(record);
        return true;
    }

    /**
     * 删除记录
     */
    @Transactional
    public boolean deleteRecord(Long recordId, String openid) {
        Optional<DivinationRecord> recordOpt = recordRepository.findById(recordId);
        if (recordOpt.isEmpty()) {
            return false;
        }

        DivinationRecord record = recordOpt.get();
        // 验证记录归属
        if (!record.getOpenid().equals(openid)) {
            log.warn("用户 {} 尝试删除不属于自己的记录 {}", openid, recordId);
            return false;
        }

        recordRepository.delete(record);
        return true;
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
    public void clearHistory(String openid) {
        log.info("清除用户历史记录: OpenID={}", openid);
        List<DivinationRecord> records = recordRepository.findTop10ByOpenidOrderByCreatedAtDesc(openid);
        // 只删除未收藏的记录
        records.stream()
                .filter(r -> !r.getIsFavorite())
                .forEach(recordRepository::delete);
    }

    /**
     * 清除用户的所有收藏记录
     */
    @Transactional
    public void clearFavorites(String openid) {
        log.info("清除用户收藏记录: OpenID={}", openid);
        List<DivinationRecord> records = recordRepository.findTop10ByOpenidOrderByCreatedAtDesc(openid);
        // 只删除收藏的记录，或取消收藏
        records.stream()
                .filter(DivinationRecord::getIsFavorite)
                .forEach(record -> {
                    record.setIsFavorite(false);
                    recordRepository.save(record);
                });
    }
}

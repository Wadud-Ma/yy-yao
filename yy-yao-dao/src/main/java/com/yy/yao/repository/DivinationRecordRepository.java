package com.yy.yao.repository;

import com.yy.yao.entity.DivinationRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 占卜记录仓储
 */
@Repository
public interface DivinationRecordRepository extends JpaRepository<DivinationRecord, Long> {

    /**
     * 根据OpenID查找所有记录 (分页)
     */
    Page<DivinationRecord> findByOpenidOrderByCreatedAtDesc(String openid, Pageable pageable);

    /**
     * 根据用户ID查找所有记录 (分页)
     */
    Page<DivinationRecord> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * 查找用户的收藏记录
     */
    Page<DivinationRecord> findByOpenidAndIsFavoriteTrueOrderByCreatedAtDesc(String openid, Pageable pageable);

    /**
     * 查找指定时间范围内的记录
     */
    @Query("SELECT d FROM DivinationRecord d WHERE d.openid = :openid " +
           "AND d.createdAt BETWEEN :startTime AND :endTime " +
           "ORDER BY d.createdAt DESC")
    List<DivinationRecord> findByOpenidAndTimeRange(
            @Param("openid") String openid,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    /**
     * 统计用户今日占卜次数
     */
    @Query("SELECT COUNT(d) FROM DivinationRecord d WHERE d.openid = :openid " +
           "AND d.createdAt >= :todayStart")
    long countTodayDivinations(@Param("openid") String openid, @Param("todayStart") LocalDateTime todayStart);

    /**
     * 统计用户总占卜次数
     */
    long countByOpenid(String openid);

    /**
     * 根据卦象ID查找记录
     */
    Page<DivinationRecord> findByOriginalHexagramIdOrderByCreatedAtDesc(Integer hexagramId, Pageable pageable);

    /**
     * 查找最近的N条记录
     */
    List<DivinationRecord> findTop10ByOpenidOrderByCreatedAtDesc(String openid);

    /**
     * 删除用户的所有记录
     */
    void deleteByOpenid(String openid);

    /**
     * 统计系统总占卜次数
     */
    @Query("SELECT COUNT(d) FROM DivinationRecord d")
    long countTotalDivinations();

    /**
     * 统计指定时间段内的占卜次数
     */
    @Query("SELECT COUNT(d) FROM DivinationRecord d WHERE d.createdAt BETWEEN :startTime AND :endTime")
    long countDivinationsByTimeRange(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}

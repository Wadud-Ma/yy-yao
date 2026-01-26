package com.yy.yao.dao.repository;

import com.yy.yao.dao.entity.DivinationRecord;
import com.yy.yao.model.domain.DivinationMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 占卜记录 Repository
 * 对应表: divination_records
 */
@Repository
public interface DivinationRecordRepository extends JpaRepository<DivinationRecord, Long> {

    /**
     * 根据用户ID查询占卜记录
     */
    Page<DivinationRecord> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * 查询用户的收藏记录
     */
    Page<DivinationRecord> findByUserIdAndIsFavoritedOrderByCreatedAtDesc(
            Long userId,
            Boolean isFavorited,
            Pageable pageable
    );

    /**
     * 根据占卜方式查询记录
     */
    Page<DivinationRecord> findByUserIdAndMethodOrderByCreatedAtDesc(
            Long userId,
            DivinationMethod method,
            Pageable pageable
    );

    /**
     * 查询用户占卜记录数量
     */
    Long countByUserId(Long userId);

    /**
     * 根据时间范围查询
     */
    Page<DivinationRecord> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            Long userId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Pageable pageable
    );

    /**
     * 查询包含特定卦象的占卜记录
     */
    @Query("SELECT dr FROM DivinationRecord dr WHERE " +
            "(dr.originalHexagramId = :hexagramId OR dr.changedHexagramId = :hexagramId) " +
            "ORDER BY dr.createdAt DESC")
    Page<DivinationRecord> findByHexagramId(@Param("hexagramId") Integer hexagramId, Pageable pageable);

    /**
     * 统计用户不同占卜方式的使用次数
     */
    @Query("SELECT dr.method, COUNT(dr) FROM DivinationRecord dr " +
            "WHERE dr.userId = :userId " +
            "GROUP BY dr.method")
    List<Object[]> countByMethod(@Param("userId") Long userId);

    /**
     * 统计卦象出现频率
     */
    @Query("SELECT dr.originalHexagramId, COUNT(dr) FROM DivinationRecord dr " +
            "GROUP BY dr.originalHexagramId " +
            "ORDER BY COUNT(dr) DESC")
    List<Object[]> statisticsHexagramUsage();

    /**
     * 统计用户今日占卜次数
     */
    @Query("SELECT COUNT(dr) FROM DivinationRecord dr WHERE dr.userId = :userId " +
            "AND dr.createdAt >= :todayStart")
    long countTodayDivinations(@Param("userId") Long userId, @Param("todayStart") LocalDateTime todayStart);

    /**
     * 统计系统总占卜次数
     */
    @Query("SELECT COUNT(dr) FROM DivinationRecord dr")
    long countTotalDivinations();

    /**
     * 根据本卦ID查询占卜记录（按创建时间倒序）
     */
    Page<DivinationRecord> findByOriginalHexagramIdOrderByCreatedAtDesc(Integer hexagramId, Pageable pageable);
}

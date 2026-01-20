package com.yy.yao.repository;

import com.yy.yao.entity.MiniProgramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 小程序用户仓储
 */
@Repository
public interface MiniProgramUserRepository extends JpaRepository<MiniProgramUser, Long> {

    /**
     * 根据OpenID查找用户
     */
    Optional<MiniProgramUser> findByOpenid(String openid);

    /**
     * 根据UnionID查找用户
     */
    Optional<MiniProgramUser> findByUnionId(String unionId);

    /**
     * 检查OpenID是否存在
     */
    boolean existsByOpenid(String openid);

    /**
     * 更新最后登录时间
     */
    @Modifying
    @Query("UPDATE MiniProgramUser u SET u.lastLoginAt = :loginTime, u.updatedAt = :loginTime WHERE u.openid = :openid")
    void updateLastLoginTime(@Param("openid") String openid, @Param("loginTime") LocalDateTime loginTime);

    /**
     * 增加用户今日占卜次数
     */
    @Modifying
    @Query("UPDATE MiniProgramUser u SET u.todayDivinationCount = u.todayDivinationCount + 1, " +
           "u.totalDivinationCount = u.totalDivinationCount + 1, " +
           "u.lastDivinationDate = :now, " +
           "u.updatedAt = :now " +
           "WHERE u.openid = :openid")
    void incrementDivinationCount(@Param("openid") String openid, @Param("now") LocalDateTime now);

    /**
     * 重置今日占卜次数
     */
    @Modifying
    @Query("UPDATE MiniProgramUser u SET u.todayDivinationCount = 0, u.updatedAt = :now " +
           "WHERE u.lastDivinationDate < :todayStart")
    int resetTodayDivinationCount(@Param("todayStart") LocalDateTime todayStart, @Param("now") LocalDateTime now);
}

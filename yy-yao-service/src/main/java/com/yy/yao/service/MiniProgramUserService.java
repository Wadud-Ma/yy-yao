package com.yy.yao.service;

import com.yy.yao.entity.MiniProgramUser;
import com.yy.yao.repository.MiniProgramUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

/**
 * 小程序用户服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MiniProgramUserService {

    private final MiniProgramUserRepository userRepository;

    /**
     * 根据OpenID查找用户
     */
    public Optional<MiniProgramUser> findByOpenid(String openid) {
        return userRepository.findByOpenid(openid);
    }

    /**
     * 创建或更新用户
     */
    @Transactional
    public MiniProgramUser createOrUpdateUser(MiniProgramUser user) {
        Optional<MiniProgramUser> existingUser = userRepository.findByOpenid(user.getOpenid());

        if (existingUser.isPresent()) {
            // 更新现有用户信息
            MiniProgramUser existing = existingUser.get();
            if (user.getNickname() != null) {
                existing.setNickname(user.getNickname());
            }
            if (user.getAvatarUrl() != null) {
                existing.setAvatarUrl(user.getAvatarUrl());
            }
            if (user.getGender() != null) {
                existing.setGender(user.getGender());
            }
            if (user.getCountry() != null) {
                existing.setCountry(user.getCountry());
            }
            if (user.getProvince() != null) {
                existing.setProvince(user.getProvince());
            }
            if (user.getCity() != null) {
                existing.setCity(user.getCity());
            }
            if (user.getPhone() != null) {
                existing.setPhone(user.getPhone());
            }
            if (user.getSessionKey() != null) {
                existing.setSessionKey(user.getSessionKey());
            }
            if (user.getUnionId() != null) {
                existing.setUnionId(user.getUnionId());
            }
            existing.setLastLoginAt(LocalDateTime.now());
            return userRepository.save(existing);
        } else {
            // 创建新用户
            user.setLastLoginAt(LocalDateTime.now());
            return userRepository.save(user);
        }
    }

    /**
     * 更新用户登录时间
     */
    @Transactional
    public void updateLastLoginTime(String openid) {
        userRepository.updateLastLoginTime(openid, LocalDateTime.now());
    }

    /**
     * 检查用户今日是否还能占卜
     */
    public boolean canDivinateToday(String openid, int maxDivinationsPerDay) {
        Optional<MiniProgramUser> userOpt = userRepository.findByOpenid(openid);
        if (userOpt.isEmpty()) {
            return true; // 新用户可以占卜
        }

        MiniProgramUser user = userOpt.get();

        // 检查是否需要重置今日次数
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        if (user.getLastDivinationDate() == null || user.getLastDivinationDate().isBefore(todayStart)) {
            return true;
        }

        return user.getTodayDivinationCount() < maxDivinationsPerDay;
    }

    /**
     * 增加用户占卜次数
     */
    @Transactional
    public void incrementDivinationCount(String openid) {
        Optional<MiniProgramUser> userOpt = userRepository.findByOpenid(openid);
        if (userOpt.isEmpty()) {
            log.warn("尝试为不存在的用户增加占卜次数: {}", openid);
            return;
        }

        MiniProgramUser user = userOpt.get();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        // 检查是否需要重置今日次数
        if (user.getLastDivinationDate() == null || user.getLastDivinationDate().isBefore(todayStart)) {
            user.setTodayDivinationCount(0);
        }

        userRepository.incrementDivinationCount(openid, now);
    }

    /**
     * 重置所有用户的今日占卜次数 (定时任务调用)
     */
    @Transactional
    public int resetAllTodayDivinationCount() {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        int count = userRepository.resetTodayDivinationCount(todayStart, LocalDateTime.now());
        log.info("重置了 {} 个用户的今日占卜次数", count);
        return count;
    }

    /**
     * 获取用户今日剩余占卜次数
     */
    public int getRemainingDivinationCount(String openid, int maxDivinationsPerDay) {
        Optional<MiniProgramUser> userOpt = userRepository.findByOpenid(openid);
        if (userOpt.isEmpty()) {
            return maxDivinationsPerDay;
        }

        MiniProgramUser user = userOpt.get();
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        // 如果最后占卜时间在今天之前，返回最大次数
        if (user.getLastDivinationDate() == null || user.getLastDivinationDate().isBefore(todayStart)) {
            return maxDivinationsPerDay;
        }

        return Math.max(0, maxDivinationsPerDay - user.getTodayDivinationCount());
    }
}

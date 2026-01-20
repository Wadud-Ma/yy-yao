package com.yy.yao.repository;

import com.yy.yao.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * 用户设置数据访问层
 */
@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
    /**
     * 根据openid查找用户设置
     * @param openid 用户openid
     * @return 用户设置
     */
    Optional<UserSettings> findByOpenid(String openid);

    /**
     * 检查用户设置是否存在
     * @param openid 用户openid
     * @return 是否存在
     */
    boolean existsByOpenid(String openid);

    /**
     * 根据openid删除用户设置
     * @param openid 用户openid
     */
    void deleteByOpenid(String openid);
}

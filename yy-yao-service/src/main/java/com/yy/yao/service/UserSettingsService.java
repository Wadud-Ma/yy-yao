package com.yy.yao.service;

import com.yy.yao.entity.UserSettings;
import com.yy.yao.repository.UserSettingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.Optional;

/**
 * 用户设置服务
 */
@Slf4j
@Service
public class UserSettingsService {

    @Autowired
    private UserSettingsRepository settingsRepository;

    /**
     * 获取用户设置
     * @param openid 用户openid
     * @return 用户设置
     */
    public Optional<UserSettings> getSettings(String openid) {
        return settingsRepository.findByOpenid(openid);
    }

    /**
     * 获取或创建用户设置（如果不存在则创建默认设置）
     * @param openid 用户openid
     * @return 用户设置
     */
    public UserSettings getOrCreateSettings(String openid) {
        return settingsRepository.findByOpenid(openid)
                .orElseGet(() -> createDefaultSettings(openid));
    }

    /**
     * 创建默认用户设置
     * @param openid 用户openid
     * @return 用户设置
     */
    @Transactional
    public UserSettings createDefaultSettings(String openid) {
        UserSettings settings = UserSettings.builder()
                .openid(openid)
                .theme("auto")
                .traditionalChinese(false)
                .soundEnabled(false)
                .vibrationEnabled(true)
                .enableAi(true)
                .autoSaveHistory(true)
                .shareAnonymous(true)
                .build();
        log.info("创建用户默认设置, openid: {}", openid);
        return settingsRepository.save(settings);
    }

    /**
     * 更新用户设置
     * @param openid 用户openid
     * @param updates 要更新的字段
     * @return 更新后的用户设置
     */
    @Transactional
    public UserSettings updateSettings(String openid, Map<String, Object> updates) {
        UserSettings settings = getOrCreateSettings(openid);

        if (updates.containsKey("theme")) {
            settings.setTheme((String) updates.get("theme"));
        }
        if (updates.containsKey("traditionalChinese")) {
            settings.setTraditionalChinese((Boolean) updates.get("traditionalChinese"));
        }
        if (updates.containsKey("soundEnabled")) {
            settings.setSoundEnabled((Boolean) updates.get("soundEnabled"));
        }
        if (updates.containsKey("vibrationEnabled")) {
            settings.setVibrationEnabled((Boolean) updates.get("vibrationEnabled"));
        }
        if (updates.containsKey("enableAi")) {
            settings.setEnableAi((Boolean) updates.get("enableAi"));
        }
        if (updates.containsKey("autoSaveHistory")) {
            settings.setAutoSaveHistory((Boolean) updates.get("autoSaveHistory"));
        }
        if (updates.containsKey("shareAnonymous")) {
            settings.setShareAnonymous((Boolean) updates.get("shareAnonymous"));
        }

        log.info("更新用户设置, openid: {}, 更新字段: {}", openid, updates.keySet());
        return settingsRepository.save(settings);
    }

    /**
     * 删除用户设置
     * @param openid 用户openid
     */
    @Transactional
    public void deleteSettings(String openid) {
        log.info("删除用户设置, openid: {}", openid);
        settingsRepository.deleteByOpenid(openid);
    }

    /**
     * 重置用户设置为默认值
     * @param openid 用户openid
     * @return 重置后的用户设置
     */
    @Transactional
    public UserSettings resetSettings(String openid) {
        log.info("重置用户设置, openid: {}", openid);
        settingsRepository.deleteByOpenid(openid);
        return createDefaultSettings(openid);
    }
}

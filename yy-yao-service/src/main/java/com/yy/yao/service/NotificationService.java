package com.yy.yao.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 通知服务
 * 参考 yi-jing-divination-app 的通知系统设计
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${notification.email.enabled:false}")
    private boolean emailEnabled;

    @Value("${notification.email.from:noreply@yy-yao.com}")
    private String fromEmail;

    @Value("${notification.admin.email:}")
    private String adminEmail;

    /**
     * 发送通知给管理员
     * 参考 yi-jing-divination-app 的 notifyOwner 实现
     *
     * @param title 标题
     * @param content 内容
     * @return 是否发送成功 (false 表示服务暂时不可用,允许降级到其他方式)
     */
    @Async
    public boolean notifyAdmin(String title, String content) {
        if (!emailEnabled) {
            log.debug("邮件通知未启用");
            return false;
        }

        if (adminEmail == null || adminEmail.isEmpty()) {
            log.warn("管理员邮箱未配置");
            return false;
        }

        // 验证输入 (参考参考项目的严格验证)
        if (title == null || title.length() > 1200) {
            throw new IllegalArgumentException("标题不能为空且长度不能超过1200字符");
        }

        if (content == null || content.length() > 20000) {
            throw new IllegalArgumentException("内容不能为空且长度不能超过20000字符");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(adminEmail);
            message.setSubject("[易经卜卦服务] " + title);
            message.setText(content);

            mailSender.send(message);

            log.info("管理员通知已发送: title={}", title);
            return true;

        } catch (Exception e) {
            log.error("发送管理员通知失败: title={}", title, e);
            // 返回 false 而非抛异常,允许调用者降级到其他通知方式
            return false;
        }
    }

    /**
     * 发送通知给特定用户
     */
    @Async
    public boolean notifyUser(String email, String title, String content) {
        if (!emailEnabled) {
            return false;
        }

        if (email == null || email.isEmpty()) {
            log.warn("用户邮箱为空");
            return false;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject(title);
            message.setText(content);

            mailSender.send(message);

            log.info("用户通知已发送: email={}, title={}", email, title);
            return true;

        } catch (Exception e) {
            log.error("发送用户通知失败: email={}, title={}", email, title, e);
            return false;
        }
    }

    /**
     * 发送欢迎邮件
     */
    public void sendWelcomeEmail(String email, String username) {
        String title = "欢迎使用易经卜卦服务";
        String content = String.format("""
                您好 %s,

                感谢您注册易经卜卦服务!

                您现在可以使用以下功能:
                - 多种卜卦方法 (铜钱法、蓍草法、数字法、时间起卦法)
                - AI 智能解读
                - 卦象历史记录
                - 经文查询

                如有任何问题,请随时联系我们。

                祝您使用愉快!

                易经卜卦服务团队
                """, username);

        notifyUser(email, title, content);
    }

    /**
     * 通知管理员新用户注册
     */
    public void notifyAdminNewUser(String username, String email) {
        String title = "新用户注册";
        String content = String.format("""
                新用户已注册:

                用户名: %s
                邮箱: %s
                注册时间: %s
                """, username, email, java.time.LocalDateTime.now());

        notifyAdmin(title, content);
    }

    /**
     * 检查通知服务是否可用
     */
    public boolean isAvailable() {
        return emailEnabled && adminEmail != null && !adminEmail.isEmpty();
    }

    // ========== DTO ==========

    @Data
    public static class NotificationPayload {
        private String title;
        private String content;
    }
}

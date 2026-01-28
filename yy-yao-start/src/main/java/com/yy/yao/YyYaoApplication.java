package com.yy.yao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 易经卜卦服务启动类
 * 由于各模块使用独立的包前缀,需要显式配置包扫描范围:
 * - ComponentScan: 扫描各模块的组件(@Component, @Service, @Controller等)
 * - EnableJpaRepositories: 扫描Repository接口
 * 注意: @EntityScan 已移除,实体类由 @SpringBootApplication 自动扫描
 */
@SpringBootApplication(scanBasePackages = {
    "com.yy.yao.core",      // 核心配置、安全基础设施
    "com.yy.yao.model",     // 业务模型、DTO、异常
    "com.yy.yao.dao",       // 数据访问层配置
    "com.yy.yao.service",   // 业务服务层
    "com.yy.yao.biz",       // 业务编排层
    "com.yy.yao.web",       // Web控制器
    "com.yy.yao"            // 启动类自身包
})
@EnableJpaRepositories(basePackages = "com.yy.yao.dao.repository")
public class YyYaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(YyYaoApplication.class, args);
    }

}

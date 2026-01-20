package com.yy.yao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 易经卜卦服务启动类
 *
 * Spring Boot会自动扫描当前包及子包下的:
 * - @Component, @Service, @Repository, @Controller等组件
 * - @Entity实体类
 * - Repository接口
 */
@SpringBootApplication
public class YyYaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(YyYaoApplication.class, args);
    }

}

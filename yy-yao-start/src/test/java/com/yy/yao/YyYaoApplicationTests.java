package com.yy.yao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.ai.openai.api-key=test-key",
    "spring.ai.openai.base-url=http://localhost:8080"
})
class YyYaoApplicationTests {

    @Test
    void contextLoads() {
        // 验证Spring上下文可以成功加载
    }

}

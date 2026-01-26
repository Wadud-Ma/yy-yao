package com.yy.yao.repository;

import com.yy.yao.dao.entity.Hexagram;
import com.yy.yao.dao.repository.HexagramRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * HexagramRepository 集成测试
 * 使用 @DataJpaTest 进行 JPA 层测试
 */
@DataJpaTest
@ActiveProfiles("test")
class HexagramRepositoryTest {

    @Autowired
    private HexagramRepository repository;

    @Test
    void testFindByBinary() {
        // 测试根据二进制编码查找卦象
        Optional<Hexagram> hexagram = repository.findByBinary("111111");

        assertTrue(hexagram.isPresent());
        assertEquals("乾", hexagram.get().getName());
        assertEquals(1, hexagram.get().getId());
    }

    @Test
    void testFindByNumber() {
        // 测试根据编号查找卦象
        Optional<Hexagram> hexagram = repository.findByNumber(2);

        assertTrue(hexagram.isPresent());
        assertEquals("坤", hexagram.get().getName());
        assertEquals("000000", hexagram.get().getSymbol());
    }

    @Test
    void testFindAll() {
        // 测试查找所有卦象
        List<Hexagram> allHexagrams = repository.findAll();

        assertNotNull(allHexagrams);
        // 应该有 64 卦
        assertTrue(allHexagrams.size() > 0);
    }

    @Test
    void testHexagramStructure() {
        // 测试卦象实体结构
        Optional<Hexagram> hexagram = repository.findByNumber(1);

        assertTrue(hexagram.isPresent());
        Hexagram qian = hexagram.get();

        assertNotNull(qian.getName());
        assertNotNull(qian.getSymbol());
        assertNotNull(qian.getJudgment());
        assertNotNull(qian.getJudgmentTranslation());
    }

    @Test
    void testNonExistentHexagram() {
        // 测试不存在的卦象
        Optional<Hexagram> nonExistent = repository.findByNumber(999);
        assertFalse(nonExistent.isPresent());

        Optional<Hexagram> invalidBinary = repository.findByBinary("999999");
        assertFalse(invalidBinary.isPresent());
    }
}

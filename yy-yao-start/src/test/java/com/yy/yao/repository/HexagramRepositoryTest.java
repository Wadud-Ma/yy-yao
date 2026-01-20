package com.yy.yao.repository;

import com.yy.yao.model.Hexagram;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HexagramRepositoryTest {

    private HexagramRepository repository;

    @BeforeEach
    void setUp() {
        repository = new HexagramRepository();
    }

    @Test
    void testFindByBinary() {
        Optional<Hexagram> qian = repository.findByBinary("111111");

        assertTrue(qian.isPresent());
        assertEquals("乾", qian.get().getName());
        assertEquals(1, qian.get().getNumber());
    }

    @Test
    void testFindByNumber() {
        Optional<Hexagram> kun = repository.findByNumber(2);

        assertTrue(kun.isPresent());
        assertEquals("坤", kun.get().getName());
        assertEquals("000000", kun.get().getBinary());
    }

    @Test
    void testFindAll() {
        List<Hexagram> allHexagrams = repository.findAll();

        assertNotNull(allHexagrams);
        assertTrue(allHexagrams.size() >= 8);
    }

    @Test
    void testHexagramStructure() {
        Optional<Hexagram> hexagram = repository.findByNumber(1);

        assertTrue(hexagram.isPresent());
        Hexagram qian = hexagram.get();

        assertNotNull(qian.getName());
        assertNotNull(qian.getBinary());
        assertNotNull(qian.getStatement());
        assertNotNull(qian.getInterpretation());
        assertNotNull(qian.getLineStatements());
        assertEquals(6, qian.getLineStatements().size());
    }

    @Test
    void testNonExistentHexagram() {
        Optional<Hexagram> nonExistent = repository.findByNumber(999);
        assertFalse(nonExistent.isPresent());

        Optional<Hexagram> invalidBinary = repository.findByBinary("999999");
        assertFalse(invalidBinary.isPresent());
    }
}

package com.yy.yao.service;

import com.yy.yao.model.DivinationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DivinationAlgorithmTest {

    private DivinationAlgorithm algorithm;

    @BeforeEach
    void setUp() {
        algorithm = new DivinationAlgorithm();
    }

    @Test
    void testCoinMethod() {
        List<DivinationResult.LineResult> lines = algorithm.coinMethod();

        assertNotNull(lines);
        assertEquals(6, lines.size());

        for (int i = 0; i < lines.size(); i++) {
            DivinationResult.LineResult line = lines.get(i);
            assertEquals(i + 1, line.getPosition());
            assertTrue(line.getValue() >= 6 && line.getValue() <= 9);
        }
    }

    @Test
    void testNumberMethod() {
        List<DivinationResult.LineResult> lines = algorithm.numberMethod();

        assertNotNull(lines);
        assertEquals(6, lines.size());

        for (DivinationResult.LineResult line : lines) {
            assertTrue(line.getValue() >= 6 && line.getValue() <= 9);
        }
    }

    @Test
    void testTimeMethod() {
        List<DivinationResult.LineResult> lines = algorithm.timeMethod();

        assertNotNull(lines);
        assertEquals(6, lines.size());
    }

    @Test
    void testCustomMethod() {
        String customLines = "789678";
        List<DivinationResult.LineResult> lines = algorithm.customMethod(customLines);

        assertNotNull(lines);
        assertEquals(6, lines.size());

        assertEquals(7, lines.get(0).getValue());
        assertEquals(8, lines.get(1).getValue());
        assertEquals(9, lines.get(2).getValue());
        assertEquals(6, lines.get(3).getValue());
        assertEquals(7, lines.get(4).getValue());
        assertEquals(8, lines.get(5).getValue());

        assertTrue(lines.get(2).isChanging());
        assertTrue(lines.get(3).isChanging());
    }

    @Test
    void testCustomMethodInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            algorithm.customMethod("12345");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            algorithm.customMethod("1234567");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            algorithm.customMethod("123456");
        });
    }

    @Test
    void testLinesToBinary() {
        String customLines = "777777";
        List<DivinationResult.LineResult> lines = algorithm.customMethod(customLines);
        String binary = algorithm.linesToBinary(lines);

        assertEquals("111111", binary);
    }

    @Test
    void testCalculateChangedBinary() {
        // 9=老阳(动,变阴), 7=少阳(不动), 9=老阳(动,变阴), 7=少阳(不动), 9=老阳(动,变阴), 7=少阳(不动)
        String customLines = "979797";
        List<DivinationResult.LineResult> lines = algorithm.customMethod(customLines);
        String changedBinary = algorithm.calculateChangedBinary(lines);

        // 老阳9变阴0, 少阳7不变1
        assertEquals("010101", changedBinary);
    }

    @Test
    void testGetChangingLinePositions() {
        // 7=少阳, 9=老阳(动), 7=少阳, 9=老阳(动), 7=少阳, 9=老阳(动)
        String customLines = "797979";
        List<DivinationResult.LineResult> lines = algorithm.customMethod(customLines);
        List<Integer> changingLines = algorithm.getChangingLinePositions(lines);

        assertEquals(3, changingLines.size());
        assertTrue(changingLines.contains(2)); // 第2位是9
        assertTrue(changingLines.contains(4)); // 第4位是9
        assertTrue(changingLines.contains(6)); // 第6位是9
    }
}

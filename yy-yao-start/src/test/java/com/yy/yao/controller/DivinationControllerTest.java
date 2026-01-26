package com.yy.yao.controller;

import com.yy.yao.web.controller.DivinationController;
import com.yy.yao.model.domain.DivinationMethod;
import com.yy.yao.model.domain.DivinationRequest;
import com.yy.yao.model.domain.DivinationResult;
import com.yy.yao.model.domain.Hexagram;
import com.yy.yao.service.impl.DivinationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DivinationControllerTest {

    @Mock
    private DivinationService divinationService;

    @InjectMocks
    private DivinationController controller;

    @Test
    void testHealthEndpoint() {
        ResponseEntity<String> response = controller.health();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("易经卜卦服务运行正常", response.getBody());
    }

    @Test
    void testConsultEndpoint() {
        DivinationResult mockResult = DivinationResult.builder()
                .timestamp(LocalDateTime.now())
                .question("测试问题")
                .method(DivinationMethod.COIN)
                .lines(new ArrayList<>())
                .changingLines(new ArrayList<>())
                .interpretation("测试解释")
                .build();

        when(divinationService.performDivination(any(DivinationRequest.class)))
                .thenReturn(mockResult);

        DivinationRequest request = new DivinationRequest();
        request.setQuestion("测试问题");
        request.setMethod(DivinationResult.DivinationMethod.COIN);

        ResponseEntity<DivinationResult> response = controller.consult(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("测试问题", response.getBody().getQuestion());
    }

    @Test
    void testGetHexagramEndpoint() {
        Hexagram mockHexagram = new Hexagram(1, "乾", "☰☰", "111111",
                "乾", "乾", "乾卦辞", "乾卦解释", "天行健", "自强不息", new ArrayList<>());

        when(divinationService.getHexagramByNumber(1))
                .thenReturn(Optional.of(mockHexagram));

        ResponseEntity<Hexagram> response = controller.getHexagram(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("乾", response.getBody().getName());
    }

    @Test
    void testGetNonExistentHexagram() {
        when(divinationService.getHexagramByNumber(999))
                .thenReturn(Optional.empty());

        ResponseEntity<Hexagram> response = controller.getHexagram(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllHexagrams() {
        List<Hexagram> mockHexagrams = new ArrayList<>();
        mockHexagrams.add(new Hexagram(1, "乾", "☰☰", "111111",
                "乾", "乾", "乾卦辞", "乾卦解释", "天行健", "自强不息", new ArrayList<>()));

        when(divinationService.getAllHexagrams()).thenReturn(mockHexagrams);

        ResponseEntity<List<Hexagram>> response = controller.getAllHexagrams();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }
}

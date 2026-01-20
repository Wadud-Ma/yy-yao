package com.yy.yao.controller;

import com.yy.yao.model.DivinationRequest;
import com.yy.yao.model.DivinationResult;
import com.yy.yao.model.Hexagram;
import com.yy.yao.service.DivinationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 卜卦API控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/divination")
@RequiredArgsConstructor
public class DivinationController {

    private final DivinationService divinationService;

    /**
     * 执行卜卦
     */
    @PostMapping("/consult")
    public ResponseEntity<DivinationResult> consult(@Valid @RequestBody DivinationRequest request) {
        log.info("收到卜卦请求: {}", request.getQuestion());

        try {
            DivinationResult result = divinationService.performDivination(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("卜卦失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 查询单个卦象
     */
    @GetMapping("/hexagram/{number}")
    public ResponseEntity<Hexagram> getHexagram(@PathVariable int number) {
        return divinationService.getHexagramByNumber(number)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 获取所有卦象列表
     */
    @GetMapping("/hexagrams")
    public ResponseEntity<List<Hexagram>> getAllHexagrams() {
        List<Hexagram> hexagrams = divinationService.getAllHexagrams();
        return ResponseEntity.ok(hexagrams);
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("易经卜卦服务运行正常");
    }
}

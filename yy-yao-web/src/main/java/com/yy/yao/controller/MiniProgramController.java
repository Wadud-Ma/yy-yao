package com.yy.yao.controller;

import com.yy.yao.dto.*;
import com.yy.yao.entity.DivinationRecord;
import com.yy.yao.entity.MiniProgramUser;
import com.yy.yao.entity.UserSettings;
import com.yy.yao.model.DivinationRequest;
import com.yy.yao.model.DivinationResult;
import com.yy.yao.model.Hexagram;
import com.yy.yao.service.DivinationRecordService;
import com.yy.yao.service.DivinationService;
import com.yy.yao.service.HexagramService;
import com.yy.yao.service.MiniProgramUserService;
import com.yy.yao.service.UserSettingsService;
import com.yy.yao.util.MiniProgramMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 小程序专用 API 控制器
 * 提供适配小程序前端的简化接口
 */
@Slf4j
@RestController
@RequestMapping("/api/mini")
@CrossOrigin(origins = "*")
public class MiniProgramController {

    @Autowired
    private DivinationService divinationService;

    @Autowired
    private HexagramService hexagramService;

    @Autowired
    private MiniProgramUserService miniProgramUserService;

    @Autowired
    private DivinationRecordService divinationRecordService;

    @Autowired
    private UserSettingsService userSettingsService;

    @Value("${app.max-divinations-per-day:10}")
    private int maxDivinationsPerDay;

    /**
     * 获取所有卦象（小程序格式）
     * GET /api/mini/hexagrams
     */
    @GetMapping("/hexagrams")
    public ApiResponse<List<MiniProgramHexagramDTO>> getAllHexagrams() {
        log.info("小程序请求: 获取所有卦象");

        List<Hexagram> hexagrams = hexagramService.getAllHexagrams();
        List<MiniProgramHexagramDTO> dtos = hexagrams.stream()
                .map(MiniProgramMapper::toMiniProgramHexagram)
                .collect(Collectors.toList());

        return ApiResponse.success(dtos);
    }

    /**
     * 根据ID获取卦象（小程序格式）
     * GET /api/mini/hexagram/{id}
     */
    @GetMapping("/hexagram/{id}")
    public ApiResponse<MiniProgramHexagramDTO> getHexagramById(@PathVariable Integer id) {
        log.info("小程序请求: 获取卦象 ID={}", id);

        Hexagram hexagram = hexagramService.getHexagramByNumber(id);
        if (hexagram == null) {
            return ApiResponse.error(404, "未找到卦象: " + id);
        }

        MiniProgramHexagramDTO dto = MiniProgramMapper.toMiniProgramHexagram(hexagram);
        return ApiResponse.success(dto);
    }

    /**
     * 根据二进制符号获取卦象（小程序格式）
     * GET /api/mini/hexagram/symbol/{symbol}
     * 例如: /api/mini/hexagram/symbol/111111 获取乾卦
     */
    @GetMapping("/hexagram/symbol/{symbol}")
    public ApiResponse<MiniProgramHexagramDTO> getHexagramBySymbol(@PathVariable String symbol) {
        log.info("小程序请求: 根据符号获取卦象 symbol={}", symbol);

        Hexagram hexagram = hexagramService.getHexagramByBinary(symbol);
        if (hexagram == null) {
            return ApiResponse.error(404, "未找到卦象: " + symbol);
        }

        MiniProgramHexagramDTO dto = MiniProgramMapper.toMiniProgramHexagram(hexagram);
        return ApiResponse.success(dto);
    }

    /**
     * 执行占卜（小程序格式）
     * POST /api/mini/divination
     */
    @PostMapping("/divination")
    public ApiResponse<MiniProgramDivinationResponse> performDivination(
            @RequestHeader(value = "X-OpenID", required = false) String openid,
            @RequestBody DivinationRequest request) {
        log.info("小程序请求: 执行占卜 - OpenID: {}, 问题: {}, 方法: {}",
                 openid, request.getQuestion(), request.getMethod());

        try {
            // 如果提供了OpenID，检查今日占卜次数限制
            if (openid != null && !openid.isEmpty()) {
                if (!miniProgramUserService.canDivinateToday(openid, maxDivinationsPerDay)) {
                    int remaining = miniProgramUserService.getRemainingDivinationCount(openid, maxDivinationsPerDay);
                    return ApiResponse.error(429, "今日占卜次数已达上限，剩余次数: " + remaining);
                }
            }

            // DivinationService 返回 DivinationResult, 需要转换为 DivinationResponse
            DivinationResult result = divinationService.performDivination(request);
            DivinationResponse response = DivinationResponse.fromDivinationResult(result);
            MiniProgramDivinationResponse dto = MiniProgramMapper.toMiniProgramResponse(response);

            // 保存占卜记录和更新用户统计
            if (openid != null && !openid.isEmpty()) {
                try {
                    divinationRecordService.saveDivinationRecord(openid, result);
                    miniProgramUserService.incrementDivinationCount(openid);
                    log.info("已保存占卜记录: OpenID={}", openid);
                } catch (Exception e) {
                    log.error("保存占卜记录失败", e);
                    // 不影响占卜结果返回
                }
            }

            return ApiResponse.success(dto);
        } catch (Exception e) {
            log.error("占卜失败", e);
            return ApiResponse.error(500, "占卜失败: " + e.getMessage());
        }
    }

    /**
     * 用户注册/登录
     * POST /api/mini/user/login
     */
    @PostMapping("/user/login")
    public ApiResponse<Map<String, Object>> userLogin(@RequestBody MiniProgramUser user) {
        log.info("小程序用户登录: OpenID={}", user.getOpenid());

        try {
            MiniProgramUser savedUser = miniProgramUserService.createOrUpdateUser(user);
            int remaining = miniProgramUserService.getRemainingDivinationCount(
                    savedUser.getOpenid(), maxDivinationsPerDay);

            Map<String, Object> result = new HashMap<>();
            result.put("user", savedUser);
            result.put("remainingDivinations", remaining);
            result.put("maxDivinationsPerDay", maxDivinationsPerDay);

            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("用户登录失败", e);
            return ApiResponse.error(500, "登录失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户信息
     * GET /api/mini/user/info
     */
    @GetMapping("/user/info")
    public ApiResponse<Map<String, Object>> getUserInfo(
            @RequestHeader("X-OpenID") String openid) {
        log.info("获取用户信息: OpenID={}", openid);

        try {
            MiniProgramUser user = miniProgramUserService.findByOpenid(openid)
                    .orElse(null);

            if (user == null) {
                return ApiResponse.error(404, "用户不存在");
            }

            int remaining = miniProgramUserService.getRemainingDivinationCount(openid, maxDivinationsPerDay);

            Map<String, Object> result = new HashMap<>();
            result.put("user", user);
            result.put("remainingDivinations", remaining);
            result.put("maxDivinationsPerDay", maxDivinationsPerDay);

            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return ApiResponse.error(500, "获取失败: " + e.getMessage());
        }
    }

    /**
     * 获取占卜历史记录（分页）
     * GET /api/mini/history?page=0&size=10
     */
    @GetMapping("/history")
    public ApiResponse<Map<String, Object>> getHistory(
            @RequestHeader("X-OpenID") String openid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("获取占卜历史: OpenID={}, page={}, size={}", openid, page, size);

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<DivinationRecord> recordPage = divinationRecordService.findUserRecords(openid, pageable);

            Map<String, Object> result = new HashMap<>();
            result.put("records", recordPage.getContent());
            result.put("totalElements", recordPage.getTotalElements());
            result.put("totalPages", recordPage.getTotalPages());
            result.put("currentPage", recordPage.getNumber());
            result.put("pageSize", recordPage.getSize());

            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("获取历史记录失败", e);
            return ApiResponse.error(500, "获取失败: " + e.getMessage());
        }
    }

    /**
     * 获取最近的占卜记录
     * GET /api/mini/history/recent
     */
    @GetMapping("/history/recent")
    public ApiResponse<List<DivinationRecord>> getRecentHistory(
            @RequestHeader("X-OpenID") String openid) {
        log.info("获取最近占卜记录: OpenID={}", openid);

        try {
            List<DivinationRecord> records = divinationRecordService.findRecentRecords(openid);
            return ApiResponse.success(records);
        } catch (Exception e) {
            log.error("获取最近记录失败", e);
            return ApiResponse.error(500, "获取失败: " + e.getMessage());
        }
    }

    /**
     * 获取收藏的占卜记录
     * GET /api/mini/favorites?page=0&size=10
     */
    @GetMapping("/favorites")
    public ApiResponse<Map<String, Object>> getFavorites(
            @RequestHeader("X-OpenID") String openid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("获取收藏记录: OpenID={}, page={}, size={}", openid, page, size);

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<DivinationRecord> recordPage = divinationRecordService.findUserFavorites(openid, pageable);

            Map<String, Object> result = new HashMap<>();
            result.put("records", recordPage.getContent());
            result.put("totalElements", recordPage.getTotalElements());
            result.put("totalPages", recordPage.getTotalPages());
            result.put("currentPage", recordPage.getNumber());
            result.put("pageSize", recordPage.getSize());

            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("获取收藏记录失败", e);
            return ApiResponse.error(500, "获取失败: " + e.getMessage());
        }
    }

    /**
     * 切换收藏状态
     * POST /api/mini/record/{id}/favorite
     */
    @PostMapping("/record/{id}/favorite")
    public ApiResponse<String> toggleFavorite(
            @RequestHeader("X-OpenID") String openid,
            @PathVariable Long id) {
        log.info("切换收藏状态: OpenID={}, recordId={}", openid, id);

        try {
            boolean success = divinationRecordService.toggleFavorite(id, openid);
            if (success) {
                return ApiResponse.success("操作成功");
            } else {
                return ApiResponse.error(404, "记录不存在或无权操作");
            }
        } catch (Exception e) {
            log.error("切换收藏状态失败", e);
            return ApiResponse.error(500, "操作失败: " + e.getMessage());
        }
    }

    /**
     * 添加备注
     * POST /api/mini/record/{id}/remark
     */
    @PostMapping("/record/{id}/remark")
    public ApiResponse<String> addRemark(
            @RequestHeader("X-OpenID") String openid,
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String remark = body.get("remark");
        log.info("添加备注: OpenID={}, recordId={}, remark={}", openid, id, remark);

        try {
            boolean success = divinationRecordService.addRemark(id, openid, remark);
            if (success) {
                return ApiResponse.success("操作成功");
            } else {
                return ApiResponse.error(404, "记录不存在或无权操作");
            }
        } catch (Exception e) {
            log.error("添加备注失败", e);
            return ApiResponse.error(500, "操作失败: " + e.getMessage());
        }
    }

    /**
     * 删除记录
     * DELETE /api/mini/record/{id}
     */
    @DeleteMapping("/record/{id}")
    public ApiResponse<String> deleteRecord(
            @RequestHeader("X-OpenID") String openid,
            @PathVariable Long id) {
        log.info("删除记录: OpenID={}, recordId={}", openid, id);

        try {
            boolean success = divinationRecordService.deleteRecord(id, openid);
            if (success) {
                return ApiResponse.success("删除成功");
            } else {
                return ApiResponse.error(404, "记录不存在或无权操作");
            }
        } catch (Exception e) {
            log.error("删除记录失败", e);
            return ApiResponse.error(500, "删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取统计信息
     * GET /api/mini/stats
     */
    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> getStats(
            @RequestHeader(value = "X-OpenID", required = false) String openid) {
        log.info("获取统计信息: OpenID={}", openid);

        try {
            Map<String, Object> stats = new HashMap<>();

            if (openid != null && !openid.isEmpty()) {
                long totalCount = divinationRecordService.countTotalDivinations(openid);
                long todayCount = divinationRecordService.countTodayDivinations(openid);
                int remaining = miniProgramUserService.getRemainingDivinationCount(openid, maxDivinationsPerDay);

                stats.put("totalDivinations", totalCount);
                stats.put("todayDivinations", todayCount);
                stats.put("remainingDivinations", remaining);
                stats.put("maxDivinationsPerDay", maxDivinationsPerDay);
            }

            long systemTotal = divinationRecordService.countSystemTotalDivinations();
            stats.put("systemTotalDivinations", systemTotal);

            return ApiResponse.success(stats);
        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            return ApiResponse.error(500, "获取失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户设置
     * GET /api/mini/settings
     */
    @GetMapping("/settings")
    public ApiResponse<UserSettings> getSettings(
            @RequestHeader("X-OpenID") String openid) {
        log.info("获取用户设置: OpenID={}", openid);

        try {
            UserSettings settings = userSettingsService.getOrCreateSettings(openid);
            return ApiResponse.success(settings);
        } catch (Exception e) {
            log.error("获取用户设置失败", e);
            return ApiResponse.error(500, "获取失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户设置
     * PATCH /api/mini/settings
     */
    @PatchMapping("/settings")
    public ApiResponse<UserSettings> updateSettings(
            @RequestHeader("X-OpenID") String openid,
            @RequestBody Map<String, Object> updates) {
        log.info("更新用户设置: OpenID={}, updates={}", openid, updates);

        try {
            UserSettings settings = userSettingsService.updateSettings(openid, updates);
            return ApiResponse.success(settings);
        } catch (Exception e) {
            log.error("更新用户设置失败", e);
            return ApiResponse.error(500, "更新失败: " + e.getMessage());
        }
    }

    /**
     * 重置用户设置为默认值
     * POST /api/mini/settings/reset
     */
    @PostMapping("/settings/reset")
    public ApiResponse<UserSettings> resetSettings(
            @RequestHeader("X-OpenID") String openid) {
        log.info("重置用户设置: OpenID={}", openid);

        try {
            UserSettings settings = userSettingsService.resetSettings(openid);
            return ApiResponse.success(settings);
        } catch (Exception e) {
            log.error("重置用户设置失败", e);
            return ApiResponse.error(500, "重置失败: " + e.getMessage());
        }
    }

    /**
     * 清除历史记录
     * DELETE /api/mini/history
     */
    @DeleteMapping("/history")
    public ApiResponse<String> clearHistory(
            @RequestHeader("X-OpenID") String openid) {
        log.info("清除历史记录: OpenID={}", openid);

        try {
            divinationRecordService.clearHistory(openid);
            return ApiResponse.success("历史记录已清除");
        } catch (Exception e) {
            log.error("清除历史记录失败", e);
            return ApiResponse.error(500, "清除失败: " + e.getMessage());
        }
    }

    /**
     * 清除收藏记录
     * DELETE /api/mini/favorites
     */
    @DeleteMapping("/favorites")
    public ApiResponse<String> clearFavorites(
            @RequestHeader("X-OpenID") String openid) {
        log.info("清除收藏记录: OpenID={}", openid);

        try {
            divinationRecordService.clearFavorites(openid);
            return ApiResponse.success("收藏记录已清除");
        } catch (Exception e) {
            log.error("清除收藏记录失败", e);
            return ApiResponse.error(500, "清除失败: " + e.getMessage());
        }
    }

    /**
     * 健康检查（小程序专用）
     * GET /api/mini/health
     */
    @GetMapping("/health")
    public ApiResponse<HealthCheckResponse> healthCheck() {
        return ApiResponse.success(new HealthCheckResponse(
                "UP",
                System.currentTimeMillis(),
                "小程序 API 正常运行"
        ));
    }

    /**
     * 健康检查响应
     */
    public static class HealthCheckResponse {
        public String status;
        public Long timestamp;
        public String message;

        public HealthCheckResponse(String status, Long timestamp, String message) {
            this.status = status;
            this.timestamp = timestamp;
            this.message = message;
        }
    }
}

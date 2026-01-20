package com.yy.yao.controller;

import com.yy.yao.entity.User;
import com.yy.yao.repository.UserRepository;
import com.yy.yao.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("用户名已存在"));
        }

        // 检查邮箱是否已存在
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("邮箱已被使用"));
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(User.Role.USER);

        userRepository.save(user);

        // 生成 JWT
        String token = jwtService.generateToken(user.getUsername());

        log.info("用户注册成功: {}", user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole().name()));
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            // 认证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();

            // 生成 JWT
            String token = jwtService.generateToken(user.getUsername());

            log.info("用户登录成功: {}", user.getUsername());
            return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole().name()));
        } catch (Exception e) {
            log.error("登录失败: {}", request.getUsername(), e);
            return ResponseEntity.badRequest().body(new ErrorResponse("用户名或密码错误"));
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.ok(new UserInfo(null, null, false));
        }

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(new UserInfo(user.getUsername(), user.getRole().name(), true));
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            log.info("用户登出: {}", username);
        }

        // 清除 Security Context
        SecurityContextHolder.clearContext();

        LogoutResponse response = new LogoutResponse(true);
        return ResponseEntity.ok(response);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(new ErrorResponse("未登录"));
        }

        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            // 更新用户信息
            if (request.getName() != null && !request.getName().isEmpty()) {
                user.setUsername(request.getName());
            }

            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                // 检查邮箱是否已被其他用户使用
                if (userRepository.existsByEmail(request.getEmail()) &&
                    !request.getEmail().equals(user.getEmail())) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("邮箱已被使用"));
                }
                user.setEmail(request.getEmail());
            }

            userRepository.save(user);

            log.info("用户信息更新成功: {}", username);

            // 返回更新后的用户信息
            UserProfileResponse response = new UserProfileResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().name()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return ResponseEntity.status(500).body(new ErrorResponse("更新失败: " + e.getMessage()));
        }
    }

    // ========== DTO ==========

    @Data
    public static class RegisterRequest {
        @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
        private String username;

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 40, message = "密码长度必须在6-40个字符之间")
        private String password;

        @Email(message = "邮箱格式不正确")
        private String email;
    }

    @Data
    public static class LoginRequest {
        @NotBlank(message = "用户名不能为空")
        private String username;

        @NotBlank(message = "密码不能为空")
        private String password;
    }

    @Data
    @RequiredArgsConstructor
    public static class AuthResponse {
        private final String token;
        private final String username;
        private final String role;
    }

    @Data
    @RequiredArgsConstructor
    public static class UserInfo {
        private final String username;
        private final String role;
        private final boolean authenticated;
    }

    @Data
    @RequiredArgsConstructor
    public static class ErrorResponse {
        private final String message;
    }

    @Data
    @RequiredArgsConstructor
    public static class LogoutResponse {
        private final boolean success;
    }

    @Data
    public static class UpdateProfileRequest {
        private String name;

        @Email(message = "邮箱格式不正确")
        private String email;

        private String avatar;
    }

    @Data
    @RequiredArgsConstructor
    public static class UserProfileResponse {
        private final Long id;
        private final String name;
        private final String email;
        private final String role;
    }
}

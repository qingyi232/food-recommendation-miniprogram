package com.campus.food.controller;

import com.campus.food.common.Result;
import com.campus.food.entity.User;
import com.campus.food.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        return Result.success(userService.login(username, password));
    }

    @PostMapping("/wxLogin")
    public Result<?> wxLogin(@RequestBody Map<String, String> params) {
        String code = params.get("code");
        return Result.success(userService.wxLogin(code));
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        return Result.success(userService.register(user));
    }

    @GetMapping("/info")
    public Result<?> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(userService.getUserInfo(userId));
    }

    @PutMapping("/update")
    public Result<?> updateUserInfo(@RequestBody User user, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        user.setId(userId);
        return Result.success(userService.updateUserInfo(user));
    }

    @PutMapping("/taste")
    public Result<?> updateTastePreference(@RequestBody Map<String, String> params, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updateTastePreference(userId, params.get("tastePreference"));
        return Result.success();
    }

    @GetMapping("/page")
    public Result<?> getUserPage(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) String keyword) {
        return Result.success(userService.getUserPage(page, size, keyword));
    }

    @PutMapping("/status/{id}")
    public Result<?> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        userService.updateUserStatus(id, params.get("status"));
        return Result.success();
    }
}

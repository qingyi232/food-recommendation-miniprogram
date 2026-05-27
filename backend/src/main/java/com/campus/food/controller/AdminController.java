package com.campus.food.controller;

import com.campus.food.common.Result;
import com.campus.food.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        return Result.success(adminService.login(username, password));
    }

    @GetMapping("/statistics")
    public Result<?> getStatistics() {
        return Result.success(adminService.getStatistics());
    }

    @GetMapping("/visualization")
    public Result<?> getVisualizationData() {
        return Result.success(adminService.getVisualizationData());
    }
}

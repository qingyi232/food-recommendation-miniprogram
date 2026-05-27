package com.campus.food.controller;

import com.campus.food.common.Result;
import com.campus.food.entity.Merchant;
import com.campus.food.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        return Result.success(merchantService.login(username, password));
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody Merchant merchant) {
        return Result.success(merchantService.register(merchant));
    }

    @GetMapping("/info")
    public Result<?> getMerchantInfo(HttpServletRequest request) {
        Long merchantId = (Long) request.getAttribute("userId");
        return Result.success(merchantService.getMerchantInfo(merchantId));
    }

    @PutMapping("/update")
    public Result<?> updateMerchantInfo(@RequestBody Merchant merchant, HttpServletRequest request) {
        Long merchantId = (Long) request.getAttribute("userId");
        merchant.setId(merchantId);
        return Result.success(merchantService.updateMerchantInfo(merchant));
    }

    @GetMapping("/page")
    public Result<?> getMerchantPage(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(required = false) String keyword,
                                     @RequestParam(required = false) Integer status) {
        return Result.success(merchantService.getMerchantPage(page, size, keyword, status));
    }

    @PutMapping("/status/{id}")
    public Result<?> updateMerchantStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        merchantService.updateMerchantStatus(id, params.get("status"));
        return Result.success();
    }
}

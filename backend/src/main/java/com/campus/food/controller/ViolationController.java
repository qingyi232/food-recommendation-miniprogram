package com.campus.food.controller;

import com.campus.food.common.Result;
import com.campus.food.entity.ViolationRecord;
import com.campus.food.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/violation")
public class ViolationController {

    @Autowired
    private ViolationService violationService;

    @PostMapping("/add")
    public Result<?> addViolation(@RequestBody ViolationRecord record, HttpServletRequest request) {
        Long adminId = (Long) request.getAttribute("userId");
        record.setAdminId(adminId);
        return Result.success(violationService.addViolation(record));
    }

    @GetMapping("/page")
    public Result<?> getViolationPage(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(required = false) String targetType,
                                      @RequestParam(required = false) Long targetId) {
        return Result.success(violationService.getViolationPage(page, size, targetType, targetId));
    }

    @PutMapping("/revoke/{id}")
    public Result<?> revokeViolation(@PathVariable Long id) {
        violationService.revokeViolation(id);
        return Result.success();
    }
}

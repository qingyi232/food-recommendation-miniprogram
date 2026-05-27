package com.campus.food.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.food.entity.ViolationRecord;

public interface ViolationService extends IService<ViolationRecord> {
    ViolationRecord addViolation(ViolationRecord record);
    IPage<ViolationRecord> getViolationPage(int page, int size, String targetType, Long targetId);
    void revokeViolation(Long violationId);
}

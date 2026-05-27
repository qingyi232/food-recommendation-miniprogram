package com.campus.food.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.food.entity.ViolationRecord;
import com.campus.food.mapper.ViolationRecordMapper;
import com.campus.food.service.MerchantService;
import com.campus.food.service.UserService;
import com.campus.food.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ViolationServiceImpl extends ServiceImpl<ViolationRecordMapper, ViolationRecord> implements ViolationService {

    @Autowired
    @Lazy
    private UserService userService;
    @Autowired
    @Lazy
    private MerchantService merchantService;

    @Override
    public ViolationRecord addViolation(ViolationRecord record) {
        record.setStatus(1);
        this.save(record);

        if ("merchant".equals(record.getTargetType())) {
            if ("disable".equals(record.getAction()) || "ban".equals(record.getAction())) {
                merchantService.updateMerchantStatus(record.getTargetId(), 2);
            }
            merchantService.addViolation(record.getTargetId());
        } else if ("user".equals(record.getTargetType())) {
            if ("disable".equals(record.getAction()) || "ban".equals(record.getAction())) {
                userService.updateUserStatus(record.getTargetId(), 0);
            }
        }
        return record;
    }

    @Override
    public IPage<ViolationRecord> getViolationPage(int page, int size, String targetType, Long targetId) {
        LambdaQueryWrapper<ViolationRecord> wrapper = new LambdaQueryWrapper<>();
        if (targetType != null && !targetType.isEmpty()) {
            wrapper.eq(ViolationRecord::getTargetType, targetType);
        }
        if (targetId != null) {
            wrapper.eq(ViolationRecord::getTargetId, targetId);
        }
        wrapper.orderByDesc(ViolationRecord::getCreateTime);
        return this.page(new Page<>(page, size), wrapper);
    }

    @Override
    public void revokeViolation(Long violationId) {
        ViolationRecord record = this.getById(violationId);
        if (record != null) {
            record.setStatus(0);
            this.updateById(record);

            if ("merchant".equals(record.getTargetType())) {
                merchantService.updateMerchantStatus(record.getTargetId(), 1);
            } else if ("user".equals(record.getTargetType())) {
                userService.updateUserStatus(record.getTargetId(), 1);
            }
        }
    }
}

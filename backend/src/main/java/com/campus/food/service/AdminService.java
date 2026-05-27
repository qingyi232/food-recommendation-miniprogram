package com.campus.food.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.food.entity.Admin;

import java.util.Map;

public interface AdminService extends IService<Admin> {
    Map<String, Object> login(String username, String password);
    Map<String, Object> getStatistics();
    Map<String, Object> getVisualizationData();
}

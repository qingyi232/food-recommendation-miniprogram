package com.campus.food.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.food.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {
    Map<String, Object> login(String username, String password);
    Map<String, Object> wxLogin(String code);
    User register(User user);
    User getUserInfo(Long userId);
    User updateUserInfo(User user);
    void updateTastePreference(Long userId, String tastePreference);
    IPage<User> getUserPage(int page, int size, String keyword);
    void updateUserStatus(Long userId, Integer status);
}

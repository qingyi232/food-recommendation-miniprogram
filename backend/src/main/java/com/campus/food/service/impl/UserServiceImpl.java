package com.campus.food.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.food.entity.User;
import com.campus.food.mapper.UserMapper;
import com.campus.food.service.UserService;
import com.campus.food.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(String username, String password) {
        String md5Password = DigestUtil.md5Hex(password);
        User user = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getPassword, md5Password));
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), "user");
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        user.setPassword(null);
        result.put("userInfo", user);
        return result;
    }

    @Override
    public Map<String, Object> wxLogin(String code) {
        // 模拟微信登录，实际需调用微信接口获取openid
        String openid = "wx_" + code;
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setUsername("wx_" + System.currentTimeMillis());
            user.setPassword(DigestUtil.md5Hex("123456"));
            user.setNickname("微信用户");
            user.setStatus(1);
            this.save(user);
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), "user");
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        user.setPassword(null);
        result.put("userInfo", user);
        return result;
    }

    @Override
    public User register(User user) {
        User existing = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
        if (existing != null) {
            throw new RuntimeException("用户名已存在");
        }
        user.setPassword(DigestUtil.md5Hex(user.getPassword()));
        user.setStatus(1);
        this.save(user);
        user.setPassword(null);
        return user;
    }

    @Override
    public User getUserInfo(Long userId) {
        User user = this.getById(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    @Override
    public User updateUserInfo(User user) {
        user.setPassword(null);
        this.updateById(user);
        return this.getById(user.getId());
    }

    @Override
    public void updateTastePreference(Long userId, String tastePreference) {
        User user = new User();
        user.setId(userId);
        user.setTastePreference(tastePreference);
        this.updateById(user);
    }

    @Override
    public IPage<User> getUserPage(int page, int size, String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(User::getUsername, keyword)
                    .or().like(User::getNickname, keyword)
                    .or().like(User::getPhone, keyword);
        }
        wrapper.orderByDesc(User::getCreateTime);
        IPage<User> result = this.page(new Page<>(page, size), wrapper);
        result.getRecords().forEach(u -> u.setPassword(null));
        return result;
    }

    @Override
    public void updateUserStatus(Long userId, Integer status) {
        User user = new User();
        user.setId(userId);
        user.setStatus(status);
        this.updateById(user);
    }
}

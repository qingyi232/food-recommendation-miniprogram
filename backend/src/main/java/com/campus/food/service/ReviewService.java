package com.campus.food.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.food.entity.Review;

public interface ReviewService extends IService<Review> {
    Review addReview(Review review);
    IPage<Review> getReviewPage(int page, int size, Long shopId, Long userId, Integer status);
    void replyReview(Long reviewId, String reply);
    void updateReviewStatus(Long reviewId, Integer status);
    void deleteReview(Long reviewId);
}

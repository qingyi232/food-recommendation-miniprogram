package com.campus.food.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.food.entity.Review;
import com.campus.food.entity.Shop;
import com.campus.food.mapper.ReviewMapper;
import com.campus.food.mapper.ShopMapper;
import com.campus.food.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private ShopMapper shopMapper;

    @Override
    public Review addReview(Review review) {
        review.setStatus(1);
        this.save(review);
        updateShopRating(review.getShopId());
        return review;
    }

    @Override
    public IPage<Review> getReviewPage(int page, int size, Long shopId, Long userId, Integer status) {
        return reviewMapper.selectReviewPage(new Page<>(page, size), shopId, userId, status);
    }

    @Override
    public void replyReview(Long reviewId, String reply) {
        Review review = new Review();
        review.setId(reviewId);
        review.setReply(reply);
        review.setReplyTime(new Date());
        this.updateById(review);
    }

    @Override
    public void updateReviewStatus(Long reviewId, Integer status) {
        Review review = new Review();
        review.setId(reviewId);
        review.setStatus(status);
        this.updateById(review);
    }

    @Override
    public void deleteReview(Long reviewId) {
        Review review = this.getById(reviewId);
        this.removeById(reviewId);
        if (review != null) {
            updateShopRating(review.getShopId());
        }
    }

    private void updateShopRating(Long shopId) {
        List<Review> reviews = this.list(new LambdaQueryWrapper<Review>()
                .eq(Review::getShopId, shopId)
                .eq(Review::getStatus, 1));
        if (!reviews.isEmpty()) {
            double avgRating = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            Shop shop = new Shop();
            shop.setId(shopId);
            shop.setRating(BigDecimal.valueOf(avgRating).setScale(2, RoundingMode.HALF_UP));
            shopMapper.updateById(shop);
        }
    }
}

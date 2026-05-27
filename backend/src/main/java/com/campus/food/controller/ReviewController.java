package com.campus.food.controller;

import com.campus.food.common.Result;
import com.campus.food.entity.Review;
import com.campus.food.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public Result<?> addReview(@RequestBody Review review, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        review.setUserId(userId);
        return Result.success(reviewService.addReview(review));
    }

    @GetMapping("/list")
    public Result<?> getReviewList(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(required = false) Long shopId,
                                   @RequestParam(required = false) Long userId,
                                   @RequestParam(required = false) Integer status) {
        return Result.success(reviewService.getReviewPage(page, size, shopId, userId, status));
    }

    @GetMapping("/user")
    public Result<?> getUserReviews(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(reviewService.getReviewPage(page, size, null, userId, null));
    }

    @PutMapping("/reply/{id}")
    public Result<?> replyReview(@PathVariable Long id, @RequestBody Map<String, String> params) {
        reviewService.replyReview(id, params.get("reply"));
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<?> updateReviewStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        reviewService.updateReviewStatus(id, params.get("status"));
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return Result.success();
    }
}

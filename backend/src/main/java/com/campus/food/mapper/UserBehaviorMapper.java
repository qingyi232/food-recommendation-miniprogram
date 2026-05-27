package com.campus.food.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.food.entity.UserBehavior;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserBehaviorMapper extends BaseMapper<UserBehavior> {

    @Select("SELECT target_id, SUM(score) as total_score FROM user_behavior " +
            "WHERE user_id = #{userId} AND target_type = #{targetType} " +
            "GROUP BY target_id ORDER BY total_score DESC LIMIT #{limit}")
    List<Map<String, Object>> selectUserPreferences(@Param("userId") Long userId,
                                                     @Param("targetType") String targetType,
                                                     @Param("limit") int limit);

    @Select("SELECT ub2.user_id, COUNT(*) as similarity FROM user_behavior ub1 " +
            "JOIN user_behavior ub2 ON ub1.target_type = ub2.target_type AND ub1.target_id = ub2.target_id " +
            "WHERE ub1.user_id = #{userId} AND ub2.user_id != #{userId} " +
            "GROUP BY ub2.user_id ORDER BY similarity DESC LIMIT #{limit}")
    List<Map<String, Object>> selectSimilarUsers(@Param("userId") Long userId, @Param("limit") int limit);

    @Select("SELECT target_id, SUM(score) as total_score FROM user_behavior " +
            "WHERE user_id IN (${userIds}) AND target_type = #{targetType} " +
            "AND target_id NOT IN (SELECT target_id FROM user_behavior WHERE user_id = #{excludeUserId} AND target_type = #{targetType}) " +
            "GROUP BY target_id ORDER BY total_score DESC LIMIT #{limit}")
    List<Map<String, Object>> selectCollaborativeItems(@Param("userIds") String userIds,
                                                       @Param("targetType") String targetType,
                                                       @Param("excludeUserId") Long excludeUserId,
                                                       @Param("limit") int limit);
}

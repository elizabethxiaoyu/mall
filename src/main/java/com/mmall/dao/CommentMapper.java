package com.mmall.dao;

import com.mmall.pojo.Cart;
import com.mmall.pojo.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CommentMapper {
    int deleteByPrimaryKey(Integer commentId);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer commentId);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Comment> selectByProductId(Integer productId);

    int deleteByUserIdProductId(@Param("userId") Integer userId,@Param("productId")  Integer productId);


}
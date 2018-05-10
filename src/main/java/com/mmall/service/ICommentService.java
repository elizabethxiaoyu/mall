package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Comment;
import com.mmall.vo.CartVo;

/**
 * Created by Eliza Liu on 2018/3/2
 */
public interface ICommentService {
    ServerResponse add(Comment comment );
    ServerResponse<PageInfo> commentList(int pageNum, int pageSize, Integer productId);
    ServerResponse deleteComment(Integer userId,Integer productId);

}

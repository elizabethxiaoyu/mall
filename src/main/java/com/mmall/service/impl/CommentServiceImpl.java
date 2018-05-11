package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.CommentMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Comment;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.service.ICommentService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Eliza Liu on 2018/3/2
 */
@Service("iCommentServie")
public class CommentServiceImpl implements ICommentService {


    @Autowired
    private CommentMapper commentMapper;



    public ServerResponse  add(Comment comment){

        if(comment !=null){
            if(StringUtils.isNotBlank(comment.getImages())){
                String[] subImageArray = comment.getImages().split(",");

            }
            if(comment.getCommentId() != null) {
                int rowCount = commentMapper.updateByPrimaryKey(comment);
                if(rowCount >0)
                    return ServerResponse.createBySuccess("更新评论成功");
                return ServerResponse.createByErrorMessage("更新评论失败");
            }else{
                int rowCount = commentMapper.insert(comment);
                if(rowCount > 0)
                    return ServerResponse.createBySuccess("新增评论成功");
                return ServerResponse.createByErrorMessage("新增评论失败");
            }
        }

        return ServerResponse.createByErrorMessage("添加或新增评论参数不正确");
    }


    public ServerResponse<PageInfo> commentList(int pageNum, int pageSize, Integer productId){
        PageHelper.startPage(pageNum,pageSize);
        List<Comment> commentList = commentMapper.selectByProductId(productId);
        PageInfo pageResult = new PageInfo();
        pageResult.setList(commentList);
        return ServerResponse.createBySuccess(pageResult);
    }


    public ServerResponse deleteComment(Integer userId,Integer productId){
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        commentMapper.deleteByUserIdProductId(userId,productId);
        return commentList(2,10,productId);
    }


}

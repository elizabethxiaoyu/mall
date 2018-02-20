package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Created by Eliza Liu on 2018/2/20
 */
public class UserServiceimpl implements IUserService{


    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        //验证用户是否存在
        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0)
            return ServerResponse.createByErrorMessage("用户名不存在");
        //密码登录MD5 ，MD5是一种不可逆的加密
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,md5Password);

        //检查用户名和密码对不对
        if(user == null)
            return ServerResponse.createByErrorMessage("密码错误");
        //todo 这里为何要设置空？
        user.setPassword(StringUtils.EMPTY);
        //用户名和密码都对
        return ServerResponse.createBySuccess("登录成功",user);

    }
    @Override
    public ServerResponse<String> register(User user){
        //校验用户名是否已经存在
        //校验email是否已存在

        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess())
            return validResponse;

        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess())
            return validResponse;

        user.setRole(Const.Role.ROLE_CUSTOMER);

        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        //写入数据库，并返回写入状态
        int resultCount = userMapper.insert(user);
        if(resultCount == 0)
            return ServerResponse.createByErrorMessage("注册失败");

        return ServerResponse.createBySuccess("注册成功");
    }
    @Override
    public ServerResponse<String> checkValid(String str, String type){
            if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
                //开始校验
                if(Const.USERNAME.equals(type)){
                    int resultCount = userMapper.checkUsername(str);
                    if(resultCount > 0)
                        return ServerResponse.createByErrorMessage("用户名已存在");
                }
                if(Const.EMAIL.equals(type)){
                    int resultCount = userMapper.checkEmail(str);
                    if(resultCount > 0)
                        return ServerResponse.createByErrorMessage("Email已存在");
                }
            }else{
                return ServerResponse.createByErrorMessage("参数错误");
            }
            return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username){
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户名不存在
            return ServerResponse.createByErrorMessage("用户不存在");

        }
        String question = userMapper.selectQuestionByUsername(username);
        if(org.apache.commons.lang3.StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }
    @Override
    public  ServerResponse<String> checkAnswer(String username, String question,String answer){
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount > 0 ){
            //说明问题及问题答案是这个用户的，且是正确的
            //todo 这里生成uuid干啥？是为了生成一个token
            String forgetToken = UUID.randomUUID().toString();
            //todo 为什么将token放入本地缓存？
            TokenCache.setKey("token_" + username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }
}

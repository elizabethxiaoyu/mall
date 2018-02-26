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
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Eliza Liu on 2018/2/20
 */
@Service("iUserService")
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

    //在点忘记密码后，会生成一个forgettoken，然后将forgettoken放入本地缓存中，起个名叫toke_username.
    //然后重置密码时，要传上面生成的forgetToken，如果不传不行，而且还要和cache里面的token对照，看看是否还有，没有的话就是无效或者过期了
    @Override
    public  ServerResponse<String> forgetRestPassword(String username, String passwordNew,String forgetToken){
        if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken))
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        //校验一下username
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户名不存在
            return ServerResponse.createByErrorMessage("用户不存在");

        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        //对cache里的token也做校验
        if(org.apache.commons.lang3.StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或过期");
        }
        if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);
            if(rowCount >0)
                return ServerResponse.createBySuccessMessage("修改密码成功");

        }else{
            return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");

    }

    @Override
    public ServerResponse<String> resetPassword( String passwordOld, String passwordNew,User user){
        //防止横向越权，要检验一下这个用户的旧密码，一定要指定是这个用户，因为我们会查询一个count（1），如果不指定id，那么结果就是true啦，count>0
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);

        if(updateCount>0)
            return ServerResponse.createBySuccessMessage("密码更新成功");
        return ServerResponse.createByErrorMessage("密码更新失败");

    }
    @Override
    public ServerResponse<User> update_information(User user){
        //username是不能被更新的
        //email也要进行校验，检查新的email是不是已经存在 ，并且如果存在的email相同的话，不能是当前我们这个用户的
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount >0 )
            return ServerResponse.createByErrorMessage("email已存在，请更换email再尝试更新");

        User  updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("更新个人信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    @Override
    public ServerResponse<User> get_information(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user ==null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        //todo 为什么将password置为空字符串
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    //backend

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    @Override
    public ServerResponse checkAdminRole(User user){
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }



}

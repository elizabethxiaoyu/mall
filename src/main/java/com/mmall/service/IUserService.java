package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by Eliza Liu
 */
public interface IUserService {
    ServerResponse<User> login(String username, String password);
    public ServerResponse<String> register(User user);
    public ServerResponse<String> checkValid(String str, String type);
    public ServerResponse<String> selectQuestion(String username);
    public  ServerResponse<String> checkAnswer(String username, String question,String answer);

}

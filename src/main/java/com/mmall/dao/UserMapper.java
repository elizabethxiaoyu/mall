package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey( Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(User user);

    int updateByPrimaryKey( User record);

    int updatePasswordByUsername(@Param("username")String username,@Param("passwordNew")String passwordNew);

    int checkUsername(@Param("username") String username);

    int checkEmail(@Param("email") String email);

    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    int checkPassword(@Param(value="password")String password,@Param("userId")Integer userId);

    int checkEmailByUserId(@Param(value="email")String email,@Param(value="userId")Integer userId);

    //sql中变量名要对应注解中的变量名
    User selectLogin(@Param("username") String username, @Param("password") String password);

    String selectQuestionByUsername(@Param("username") String username);






}
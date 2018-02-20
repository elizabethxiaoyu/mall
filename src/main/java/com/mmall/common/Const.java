package com.mmall.common;

/**
 * Created by Eliza Liu on 2018/2/20
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    //定义接口的目的是，既不如枚举那么繁重，有可以起到分组的作用
    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1;     //管理员
    }
}

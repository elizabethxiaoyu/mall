package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Eliza Liu on 2018/2/20
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";


    public interface  ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }
    //定义接口的目的是，既不如枚举那么繁重，有可以起到分组的作用
    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1;     //管理员
    }

    public interface Cart{
        int CHECKED = 1;
        int UN_CHECKED = 0; //购物车中未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCESS = "LIMIT_NUM_SUCESS";
    }
    public enum ProductStatusEnum{
        ON_SALE(1,"在线");
        private String value;
        private int code;

        ProductStatusEnum(int code,String value) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }


}

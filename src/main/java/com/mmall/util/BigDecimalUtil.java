package com.mmall.util;

import java.math.BigDecimal;

/**
 * 这个类是便于  在进行小数运算时，要使用BigDecimal来避免精度丢失问题，同时不用很麻烦的进行类型转换
 * Created by Eliza Liu on 2018/3/2
 */
public class BigDecimalUtil {


    private BigDecimalUtil(){

    }
    public static BigDecimal add(double d1, double d2){
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.add(b2);
    }
    public static BigDecimal sub(double d1, double d2){
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.subtract(b2);
    }
    public static BigDecimal mul(double d1, double d2){
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.multiply(b2);
    }
    public static BigDecimal div(double d1, double d2){
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));

        //考虑除不尽的情况，四舍五入,保留两位小数
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);
    }
}

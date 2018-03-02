package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Cart;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * Created by Eliza Liu on 2018/3/2
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add (HttpSession session, Integer count, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }else{
            return iCartService.add(user.getId(),productId,count);
        }
    }


    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update (HttpSession session, Integer productId,Integer count){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }else{
            return iCartService.update(user.getId(),productId,count);
        }
    }

    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerResponse deleteProduct (HttpSession session, String productIds ){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }else{
            return iCartService.deleteProduct(user.getId(),productIds);
        }
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list (HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }else{
            return iCartService.list(user.getId());
        }
    }

    //全选
    //全反选

    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse selectAll (HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }else{
            return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);
        }
    }

    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse un_SelectAll (HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }else{
            return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
        }
    }

    //单独选某个产品
    //单独反选

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse select (HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }else{
            return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.CHECKED);
        }
    }

    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse un_Select (HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }else{
            return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.UN_CHECKED);
        }
    }

    //查询当前用户的购物车的产品数量，如果一个产品有10个，那么数量就是10个

    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createBySuccess(0);

        }else{
            return iCartService.getCartProductCount(user.getId());
        }
    }


}

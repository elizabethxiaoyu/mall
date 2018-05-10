package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.impl.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Eliza Liu on 2018/3/4
 */

@Controller
@RequestMapping("/order/")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private IOrderService orderService;

    @RequestMapping("create.do")
    @ResponseBody
    public ServerResponse create(HttpSession session, Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }else{
           return orderService.createOrder(user.getId(),shippingId);
        }

    }


    @RequestMapping("cancel.do")
    @ResponseBody
    public ServerResponse create(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }else{
            return orderService.cancel(user.getId(),orderNo);
        }

    }


    //获取购物车中的产品  预览
    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }else{
            return orderService.getOrderCartProduct(user.getId());
        }

    }


    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpSession session,Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }else{
            return orderService.getOrderDetail(user.getId(),orderNo);
        }

    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }else{
            return orderService.getOrderList(user.getId(),pageNum,pageSize);
        }

    }



    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }else{
           String path = request.getSession().getServletContext().getRealPath("update");
            return orderService.pay(orderNo,user.getId(),path);
        }

    }



    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){
        Map<String ,String> params = Maps.newHashMap();
        Map requestParams = request.getParameterMap();
        for(Iterator iterator = requestParams.keySet().iterator();iterator.hasNext();){
            String name =(String) iterator.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            //拼接所有的value到一个string
            for(int i = 0 ;i < values.length;i++){
                valueStr = (i==values.length-1)?valueStr +values[i]:valueStr +values[i]+",";
            }
            params.put(name,valueStr);
        }
        logger.info("支付宝回调，sign:{},trade_status:{},参数：{}",params.get("sign"),params.get("trade_status"),params.toString());
        //非常重要，验证回调的正确性，是不是支付宝发的，还要避免重复通知
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            //todo 验证各种数据

            if(!alipayRSACheckedV2){
                return ServerResponse.createByErrorMessage("非法请求，严重不通过，再恶意请求，将要报警");
            }

            ServerResponse serverResponse = orderService.aliCallback(params);
            if(serverResponse.isSuccess()){
                return Const.AlipayCallback.RESPONSE_SUCCESS;
            }else{
                return Const.AlipayCallback.RESPONSE_FAILED;
            }

        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常",e);
            e.printStackTrace();
        }
        //todo 这里我不知道对不对
        return ServerResponse.createByErrorMessage("回调错误");
    }

    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = orderService.queryOrderPayStatus(user.getId(),orderNo);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySuccess(true);
        }else{
            return ServerResponse.createBySuccess(false);
        }

    }


}

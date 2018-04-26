package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.OrderItem;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IOrderItemService;
import com.mmall.service.IOrderService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.vo.OrderItemVo;
import com.mmall.vo.OrderVo;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Eliza Liu on 2018/4/24
 */
@Controller
@RequestMapping("/manage/orderitem")

public class OrderItemManageController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IOrderItemService iOrderItemService;

    @Autowired
    private ProductMapper productMapper;
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderItemList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }
        return iOrderItemService.manageListByOwnerId(user.getId(),pageNum,pageSize);

    }

    /**
     * 注意横向越权
     * @param session
     * @param orderItemNo
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderItemVo> detail(HttpSession session, Integer orderItemNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

        if(isValid(user.getId(),orderItemNo)){
            return iOrderItemService.manageDetail(orderItemNo);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }

    }

    /**
     * 注意横向越权
     * @param session
     * @param orderItemNo
     * @return
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<OrderItemVo> search(HttpSession session, Integer orderItemNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }

        if(isValid(user.getId(),orderItemNo)){
            //是该商品的管理员，增加逻辑
            return iOrderItemService.manageSearch(orderItemNo);

        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }

    }

    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<String> orderSendGoods (HttpSession session, Long orderNo, Integer orderItemNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());

        }

        if(isValid(user.getId(),orderItemNo)){
            return iOrderItemService.manageSendGoods(orderNo,orderItemNo);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }

    }

    /**
     * 权限判断
     * @param ownerId
     * @param orderItemNo
     * @return
     */
    private boolean isValid(Integer ownerId, Integer orderItemNo){
        boolean valid = false;

        Integer productId = iOrderItemService.selectProductIdByOrderItem(orderItemNo).getData();

        Product product = productMapper.selectByPrimaryKey(productId);

        if(product.getOwnerId() == ownerId)
            return true;
        return false;

    }

}

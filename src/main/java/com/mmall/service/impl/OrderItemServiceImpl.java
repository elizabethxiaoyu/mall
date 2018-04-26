package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.OrderItemMapper;
import com.mmall.dao.OrderMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Order;
import com.mmall.pojo.OrderItem;
import com.mmall.pojo.Product;
import com.mmall.service.IOrderItemService;
import com.mmall.util.DateTimeUtil;
import com.mmall.vo.OrderItemVo;
import com.mmall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Eliza Liu on 2018/4/24
 */

@Service("iOrderItemService")
public class OrderItemServiceImpl implements IOrderItemService {
    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ProductMapper productMapper;

        public ServerResponse<PageInfo> manageListByOwnerId(Integer ownerId,int pageNum, int pageSize){
            PageHelper.startPage(pageNum,pageSize);
            List<Integer> productList = productMapper.selectProductIdsByOwnerId(ownerId);

            List<OrderItem> orderItemList = orderItemMapper.selectByProductIds(productList);

            List<OrderItemVo> orderItemVoList = Lists.newArrayList();
            for(OrderItem  orderItem: orderItemList){
                OrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
                orderItemVoList.add(orderItemVo);
            }


            PageInfo pageResult = new PageInfo(orderItemVoList);
            pageResult.setList(orderItemVoList);
            return ServerResponse.createBySuccess(pageResult);

    }

    public ServerResponse<OrderItemVo> manageDetail(Integer orderItemId){
        OrderItem orderItem = orderItemMapper.selectByPrimaryKey(orderItemId);
        if(orderItem == null)
            return ServerResponse.createByErrorMessage("此订单项不存在");
        return ServerResponse.createBySuccess(assembleOrderItemVo(orderItem));
    }


    public ServerResponse<OrderItemVo> manageSearch(Integer orderItemNo){

        OrderItem orderItem = orderItemMapper.selectByPrimaryKey(orderItemNo);
        if(orderItem == null){
            return ServerResponse.createByErrorMessage("此订单项不存在");
        }

        OrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
        return ServerResponse.createBySuccess(orderItemVo);

    }

    public ServerResponse<String> manageSendGoods(Long orderNo, Integer orderItemNo){
        OrderItem orderItem = orderItemMapper.selectByPrimaryKey(orderItemNo);
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null)
            return ServerResponse.createByErrorMessage("该订单不存在");

        if(orderItem == null)
            return ServerResponse.createByErrorMessage("该订单项不存在");

        if(order.getStatus() == Const.OrderStatusEnum.PAID.getCode()){
            orderItem.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
            orderItem.setSend_time(new java.util.Date());
            orderItemMapper.updateByPrimaryKeySelective(orderItem);

        }
        return ServerResponse.createBySuccess("发货成功");
    }
    /**
     * 获得订单项对应的商品id
     * @param orderItemId
     * @return
     */
    public ServerResponse<Integer>  selectProductIdByOrderItem(Integer orderItemId){
        OrderItem orderItem = orderItemMapper.selectByPrimaryKey(orderItemId);
        if(orderItem== null){
            return ServerResponse.createByErrorMessage("此订单项不存在");
        }
        return ServerResponse.createBySuccess(orderItem.getProductId());
    }

    private OrderItemVo assembleOrderItemVo(OrderItem orderItem){
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderItemNo(orderItem.getId());
        orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        orderItemVo.setStatus(orderItem.getStatus());
        orderItemVo.setSend_time(DateTimeUtil.dateToStr(orderItem.getSend_time()));
        return orderItemVo;


    }

}

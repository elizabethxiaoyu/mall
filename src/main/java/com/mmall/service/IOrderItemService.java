package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.vo.OrderItemVo;


/**
 * Created by Eliza Liu on 2018/4/24
 */
public interface IOrderItemService {

    ServerResponse<PageInfo> manageListByOwnerId(Integer ownerId, int pageNum, int pageSize);
    ServerResponse<Integer>  selectProductIdByOrderItem(Integer orderItemId);
    ServerResponse<OrderItemVo> manageDetail(Integer orderItemId);
    ServerResponse<OrderItemVo> manageSearch(Integer orderItemNo);
    ServerResponse<String> manageSendGoods(Long orderNo,Integer orderItemNo);
}

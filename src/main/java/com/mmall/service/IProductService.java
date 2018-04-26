package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.Shipping;
import com.mmall.vo.ProductDetailVo;

/**
 * Created by Eliza Liu on 2018/2/28
 */
public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);
    ServerResponse<String> setSaleStatus(Integer productId, Integer status);
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);
    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId, int pageNum, int pageSize,String orderBy);
    ServerResponse<Integer> getOwnerId(Integer productId);
    ServerResponse<PageInfo>getProductListByOwnerId (Integer userId,int pageNum, int pageSize);

}

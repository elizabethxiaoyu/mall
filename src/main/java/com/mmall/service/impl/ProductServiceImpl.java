package com.mmall.service.impl;

import ch.qos.logback.classic.gaffer.PropertyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.controller.backend.ProductManageController;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eliza Liu on 2018/2/28
 */

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    public ServerResponse saveOrUpdateProduct(Product product){
        if(product !=null){
            if(StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray = product.getSubImages().split(",");
                if(subImageArray.length > 0){
                    product.setMainImage(subImageArray[0]);
                }
            }
            if(product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if(rowCount >0)
                    return ServerResponse.createBySuccess("更新产品成功");
                return ServerResponse.createByErrorMessage("更新产品失败");
            }else{
                int rowCount = productMapper.insert(product);
                if(rowCount > 0)
                    return ServerResponse.createBySuccess("新增产品成功");
                return ServerResponse.createByErrorMessage("新增产品失败");
            }
        }

        return ServerResponse.createByErrorMessage("添加或新增产品参数不正确");

    }

    public ServerResponse<Integer> getOwnerId(Integer productId){
        return ServerResponse.createBySuccess(productMapper.selectOwnerIdByProductId(productId));
    }

    public ServerResponse<String> setSaleStatus(Integer productId, Integer status){
        if(productId == null || status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount >0){
            return ServerResponse.createBySuccessMessage("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");
    }

    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            //todo 这里下架的查不到吗
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }

        //先用vo来组装业务对象
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }


    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        //imageHost  这是为了防止硬编码，只改property文件就可以，并且可以热部署
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        //parentCategory
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setCategoryId(0);
        }else{
            productDetailVo.setCategoryId(category.getParentId());
        }
        //creatTime
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        //updateTime
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }

    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize){
        //startPage
        //填充自己的sql查询逻辑
        //pageHelper 收尾
        PageHelper.startPage(pageNum,pageSize);

        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVoList = Lists.newArrayList();

        for(Product productItem : productList){
            ProductListVo productListVo = assessmbleProductListVo(productItem);
            productListVoList.add(productListVo);
        }

        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    public ServerResponse<PageInfo> getProductListByOwnerId(Integer userId,int pageNum, int pageSize){
        //startPage
        //填充自己的sql查询逻辑
        //pageHelper 收尾
        PageHelper.startPage(pageNum,pageSize);

        List<Product> productList = productMapper.selectListByOwnerId(userId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        System.out.println("productList:  " + productList.size()  );

        for(Product productItem : productList){
            ProductListVo productListVo = assessmbleProductListVo(productItem);
            productListVoList.add(productListVo);
        }

        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }
    private ProductListVo assessmbleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setStatus(product.getStatus());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setPrice(product.getPrice());
        productListVo.setOwnerId(product.getOwnerId());
        return productListVo;
    }

    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();

        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName,productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();

        for(Product productItem : productList){
            ProductListVo productListVo = assessmbleProductListVo(productItem);
            productListVoList.add(productListVo);
        }

        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);

    }

    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId){
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){

            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        if(product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()){
            return ServerResponse.createByErrorMessage("产品已下架或删除");

        }

        //先用vo来组装业务对象
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId, int pageNum, int pageSize,String orderBy){
        if(StringUtils.isBlank(keyword) && categoryId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = new ArrayList<Integer>(); //这个集合用于当传入的分类是较高层次的分类时，要递归遍历所有的子分类

        if(categoryId != null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category == null && StringUtils.isBlank(keyword)){
                //没有该分类，并且关键字为空，这时返回一个空的结果集，不报错
                PageHelper.startPage(pageNum,pageSize);
                List<ProductDetailVo> productDetailVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productDetailVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            //递归获取了包含子目录的所有目录
            categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }
        if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum,pageSize);
        //排序处理
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
        //找到产品集合，并生成用户可见的产品
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product product : productList){
            ProductListVo productListVo = assessmbleProductListVo(product);
            productListVoList.add(productListVo);
        }

        //分页
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}

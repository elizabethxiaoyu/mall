package com.mmall.service;

import com.mmall.common.ServerResponse;

/**
 * Created by Eliza Liu on 2018/2/27
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);
    ServerResponse updateCategoryName (Integer categoryId, String categoryName);
}

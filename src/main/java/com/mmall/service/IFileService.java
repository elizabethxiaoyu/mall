package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Eliza Liu on 2018/3/1
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}

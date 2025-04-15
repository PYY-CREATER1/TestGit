package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/admin/common")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传",file);

        try {
            //获取文件名后缀
            String filename = file.getOriginalFilename();
            String extension = filename.substring(filename.lastIndexOf("."));
            //使用uuid,防止文件重名
            String objectName = UUID.randomUUID().toString()+extension;
            aliOssUtil.upload(file.getBytes(),objectName);
            return Result.success(objectName);
        } catch (IOException e) {
            log.error("文件上传失败,{}",e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}

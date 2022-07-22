package com.yrc.emos.wx.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.yrc.emos.wx.config.shiro.JWTUtil;
import com.yrc.emos.wx.exception.EmosException;
import com.yrc.emos.wx.service.FaceModelService;
import com.yrc.emos.wx.util.R;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Description:
 * User: joker
 * Date: 2022-05-26-15:08
 * Time: 15:08
 */
@Log4j2
@RequestMapping("/faceModel")
@RestController
@Api("创建人脸模型的Web接口")
public class FaceModelController {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private FaceModelService faceModelService;


    @Value("${emos.image-folder}")
    private String imageFolder;

    @PostMapping("createFaceModel")
    public R createFaceModel(@RequestPart("photo") MultipartFile file, @RequestHeader("token") String token) {
        if (file.isEmpty()) {
            return R.error("没有上传图片");
        }

        // 将图片保存到临时文件夹
        String photoName = file.getOriginalFilename();
        if (!StrUtil.endWithIgnoreCase(photoName, ".jpg")) {
            return R.error("必须提交JPG格式图片");
        }

        String photoPath = imageFolder + File.separator + photoName;

        try {
            file.transferTo(new File(photoPath));
            Integer userId = jwtUtil.getUserId(token);
            faceModelService.createFaceModel(userId, photoPath);
            return R.ok("人脸建模成功");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new EmosException("人脸建模异常");
        } finally {
            // 删除临时图片
            FileUtil.del(photoPath);
        }

    }
}

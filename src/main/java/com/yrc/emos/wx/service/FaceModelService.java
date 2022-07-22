package com.yrc.emos.wx.service;

import com.yrc.emos.wx.pojo.entity.FaceModelEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
public interface FaceModelService extends IService<FaceModelEntity> {


    void createFaceModel(Integer userId, String photoPath);

    String getFaceModelByUserId(Integer userId);

    void saveFaceModel(FaceModelEntity faceModel);

    int deleteFaceModel(Integer userId);

}

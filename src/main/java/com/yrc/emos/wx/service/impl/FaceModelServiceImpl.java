package com.yrc.emos.wx.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrc.emos.wx.pojo.entity.FaceModelEntity;
import com.yrc.emos.wx.exception.EmosException;
import com.yrc.emos.wx.mapper.FaceModelMapper;
import com.yrc.emos.wx.service.FaceModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.yrc.emos.wx.constants.EmosConstants.FaceModelResponse;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Service
public class FaceModelServiceImpl extends ServiceImpl<FaceModelMapper, FaceModelEntity> implements FaceModelService {


    @Value("${emos.face.create-face-model-url}")
    private String createFaceModelUrl;


    @Value("${emos.code}")
    private String code;

    @Autowired
    private FaceModelMapper faceModelDao;

    /**
     * 创建人脸模型数据
     */
    @Override
    @Transactional
    public void createFaceModel(Integer userId, String photoPath) {
        // 向人脸识别程序发送创建人脸模型请求
        HttpRequest request = HttpUtil.createPost(createFaceModelUrl);
        request.form("photo", FileUtil.file(photoPath));
        request.form("code", code);
        HttpResponse response = request.execute();
        String body = response.body();
        if (FaceModelResponse.UNRECOGNIZED_FACE.equals(body) || FaceModelResponse.MULTIPLE_FACES.equals(body)) {
            throw new EmosException(body);
        }
        // 保存人脸模型
        FaceModelEntity faceModel = new FaceModelEntity();
        faceModel.setUserId(userId);
        faceModel.setFaceModel(body);
        saveFaceModel(faceModel);
    }

    /**
     * 查询指定用户的人脸模型
     */
    @Override
    public String getFaceModelByUserId(Integer userId) {
        FaceModelEntity faceModel = baseMapper.selectOne(new QueryWrapper<FaceModelEntity>()
                .select("face_model").eq("user_id", userId));
        return faceModel == null ? null : faceModel.getFaceModel();
    }

    /**
     * 保存用户的人脸模型
     */
    @Override
    @Transactional
    public void saveFaceModel(FaceModelEntity faceModel) {
        //baseMapper.insert(faceModel);
        faceModelDao.insert(faceModel);
    }


    /**
     * 删除指定用户的人脸模型
     */
    @Override
    @Transactional
    public int deleteFaceModel(Integer userId) {
        //return baseMapper.delete(new QueryWrapper<FaceModelEntity>().eq("user_id", userId));
        return faceModelDao.delete(new QueryWrapper<FaceModelEntity>().eq("user_id", userId));
    }

}

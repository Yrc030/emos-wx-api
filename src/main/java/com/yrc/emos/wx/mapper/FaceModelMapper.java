package com.yrc.emos.wx.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yrc.emos.wx.pojo.entity.FaceModelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Mapper
public interface FaceModelMapper extends BaseMapper<FaceModelEntity> {

    default Boolean deleteByUserId(Integer userId){
        return delete(new QueryWrapper<FaceModelEntity>().eq("user_id", userId)) > 0;
    }
}

package com.yrc.emos.wx.mapper;

import com.yrc.emos.wx.pojo.entity.SysConfigEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfigEntity> {
    List<SysConfigEntity> selectSysConfigs();
}

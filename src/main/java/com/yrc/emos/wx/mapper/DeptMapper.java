package com.yrc.emos.wx.mapper;

import com.yrc.emos.wx.pojo.result.DeptWithMembersResult;
import com.yrc.emos.wx.pojo.entity.DeptEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface DeptMapper extends BaseMapper<DeptEntity> {

    List<DeptWithMembersResult> selectWithMembersCount(@Param("keyword") String keyword);
}

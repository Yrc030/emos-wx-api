package com.yrc.emos.wx.mapper;

import com.yrc.emos.wx.pojo.entity.RoleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleEntity> {

    List<LinkedHashMap<String,Object>> selectRoleOwnPermission(@Param("roleId") Integer roleId);

    List<LinkedHashMap<String,Object>> selectAllPermission();

    Long selectUserRoleCount(@Param("roleId") Integer roleId);
}

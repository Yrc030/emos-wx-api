package com.yrc.emos.wx.service;

import com.yrc.emos.wx.pojo.entity.RoleEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrc.emos.wx.pojo.result.RolePermissionResult;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
public interface RoleService extends IService<RoleEntity> {

   Collection<RolePermissionResult> getRoleOwnPermission(Integer roleId);

   Collection<RolePermissionResult> getAllPermission();

   List<RoleEntity> getAllRole();

   Long getUserRoleCount(Integer roleId);

   void deleteRoleById(Integer roleId);

   void saveRole(RoleEntity entity);

   void updateRole(RoleEntity entity);
}

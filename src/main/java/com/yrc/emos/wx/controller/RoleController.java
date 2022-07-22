package com.yrc.emos.wx.controller;

import com.yrc.emos.wx.constraints.group.InsertGroup;
import com.yrc.emos.wx.constraints.group.UpdateGroup;
import com.yrc.emos.wx.exception.EmosException;
import com.yrc.emos.wx.pojo.entity.RoleEntity;
import com.yrc.emos.wx.pojo.form.DeleteRoleByIdForm;
import com.yrc.emos.wx.pojo.result.RolePermissionResult;
import com.yrc.emos.wx.service.RoleService;
import com.yrc.emos.wx.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-07-13-11:31
 * Time: 11:31
 */
@Api("角色模块")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;


    @GetMapping("/getRoleOwnPermission")
    @ApiOperation("获取角色拥有的权限")
    @RequiresPermissions(value = {"ROOT", "ROLE:SELECT"}, logical = Logical.OR)
    public R getRoleOwnPermission(@RequestParam("roleId") Integer roleId) {
        if (roleId == null || roleId < 0) {
            throw new EmosException("角色id错误");
        }
        Collection<RolePermissionResult> result = roleService.getRoleOwnPermission(roleId);
        return R.ok().put("result", result);
    }


    @GetMapping("getAllPermission")
    @ApiOperation("获取所有的权限")
    @RequiresPermissions(value = {"ROOT", "ROLE:SELECT"}, logical = Logical.OR)
    public R getAllPermission() {
        Collection<RolePermissionResult> result = roleService.getAllPermission();
        return R.ok().put("result", result);
    }


    @GetMapping("/getAllRole")
    @ApiOperation("获取用户所有的角色")
    @RequiresPermissions(value = {"ROOT", "ROLE:SELECT"}, logical = Logical.OR)
    public R getAllRole() {
        List<RoleEntity> result = roleService.getAllRole();
        return R.ok().put("result", result);
    }


    @PostMapping("/saveRole")
    @ApiOperation("新增角色")
    @RequiresPermissions(value = {"ROOT", "ROLE:INSERT"}, logical = Logical.OR)
    public R saveRole(@Validated(InsertGroup.class) @RequestBody RoleEntity entity) {
        roleService.saveRole(entity);
        return R.ok("保存成功");
    }


    @PostMapping("/updateRole")
    @ApiOperation("更新角色")
    @RequiresPermissions(value = {"ROOT", "ROLE:UPDATE"}, logical = Logical.OR)
    public R updateRole(@Validated(UpdateGroup.class) @RequestBody RoleEntity entity) {
        roleService.updateRole(entity);
        return R.ok("修改成功");
    }


    @PostMapping("/deleteRoleById")
    @ApiOperation("删除指定角色")
    @RequiresPermissions(value = {"ROOT", "ROLE:DELETE"}, logical = Logical.OR)
    public R deleteRoleById(@Validated @RequestBody DeleteRoleByIdForm form) {
        roleService.deleteRoleById(form.getId());
        return R.ok("删除成功");
    }
}

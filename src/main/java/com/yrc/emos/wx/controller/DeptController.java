package com.yrc.emos.wx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yrc.emos.wx.constraints.group.InsertGroup;
import com.yrc.emos.wx.constraints.group.UpdateGroup;
import com.yrc.emos.wx.pojo.entity.DeptEntity;
import com.yrc.emos.wx.service.DeptService;
import com.yrc.emos.wx.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description:
 * User: joker
 * Date: 2022-07-14-9:21
 * Time: 9:21
 */
@RestController
@Api("部门管理模块的web接口")
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @ApiOperation("获取所有部门")
    @GetMapping("/getAllDept")
    public R getAllDept() {
        List<DeptEntity> result = deptService.getAllDept();
        return R.ok().put("result", result);
    }

    @ApiOperation("新增部门")
    @PostMapping("/saveDept")
    @RequiresPermissions(value = {"ROOT", "DEPT:INSERT"}, logical = Logical.OR)
    public R saveDept(@Validated(InsertGroup.class) @RequestBody DeptEntity entity) {
        deptService.saveDept(entity);
        return R.ok("保存成功");
    }

    @ApiOperation("修改部门")
    @PostMapping("/updateDept")
    @RequiresPermissions(value = {"ROOT", "DEPT:UPDATE"}, logical = Logical.OR)
    public R updateDept(@Validated(UpdateGroup.class) @RequestBody DeptEntity entity) {
        deptService.updateDept(entity);
        return R.ok("修改成功");
    }


    @ApiOperation("修改部门")
    @PostMapping("/deleteDept")
    @RequiresPermissions(value = {"ROOT", "DEPT:DELETE"}, logical = Logical.OR)
    public R deleteDept(@Validated @RequestBody DeptEntity entity) {
        deptService.deleteDept(entity.getId());
        return R.ok("删除成功");
    }
}

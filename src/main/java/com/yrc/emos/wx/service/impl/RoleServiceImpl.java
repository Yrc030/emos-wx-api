package com.yrc.emos.wx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrc.emos.wx.exception.EmosException;
import com.yrc.emos.wx.mapper.RoleMapper;
import com.yrc.emos.wx.pojo.entity.RoleEntity;
import com.yrc.emos.wx.pojo.result.RolePermissionResult;
import com.yrc.emos.wx.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.util.*;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements RoleService {
    @Autowired
    private RoleMapper roleDao;

    @Override
    public Collection<RolePermissionResult> getRoleOwnPermission(Integer roleId) {
        List<LinkedHashMap<String, Object>> maps = roleDao.selectRoleOwnPermission(roleId);
        return handleRolePermissionResult(maps);
    }

    @Override
    public Collection<RolePermissionResult> getAllPermission() {
        List<LinkedHashMap<String, Object>> maps = roleDao.selectAllPermission();
        return handleRolePermissionResult(maps);
    }

    private Collection<RolePermissionResult> handleRolePermissionResult(List<LinkedHashMap<String, Object>> maps) {
        LinkedHashMap<String, RolePermissionResult> resultMap = new LinkedHashMap<>();
        maps.forEach(m -> {
            String key = m.get("moduleName").toString();
            RolePermissionResult r = resultMap.get(key);
            if (r == null) {
                r = new RolePermissionResult();
                BeanUtil.copyProperties(m, r);
            }
            RolePermissionResult.ActionResult ar = new RolePermissionResult.ActionResult();
            BeanUtil.copyProperties(m, ar);

            List<RolePermissionResult.ActionResult> action = r.getAction();
            if (CollectionUtil.isEmpty(action)) {
                action = new ArrayList<>();
            }
            action.add(ar);
            r.setAction(action);
            resultMap.put(key, r);
        });
        return resultMap.values();
    }

    @Override
    public List<RoleEntity> getAllRole() {
        return list(new QueryWrapper<RoleEntity>()
                .select("id", "role_name", "systemic")
                .orderByAsc("id"));
    }

    @Override
    public Long getUserRoleCount(Integer roleId) {
        return roleDao.selectUserRoleCount(roleId);
    }

    @Override
    @Transactional
    public void deleteRoleById(Integer roleId) {
        Long count = getUserRoleCount(roleId);
        if (count > 0) {
            throw new EmosException("该角色关联着用户，所以无法删除");
        }
        boolean deleted = remove(new QueryWrapper<RoleEntity>()
                .eq("id", roleId)
                .eq("systemic", false));
        if (!deleted) {
            throw new EmosException("角色删除失败");
        }
    }

    @Override
    @Transactional
    public void saveRole(RoleEntity entity) {
        Arrays.sort(entity.getPermissions());
        int count = roleDao.insert(entity);
        if (count != 1) {
            throw new EmosException("保存角色失败");
        }
    }

    @Override
    @Transactional
    public void updateRole(RoleEntity entity) {
        Arrays.sort(entity.getPermissions());
        int count = roleDao.update(entity, new UpdateWrapper<RoleEntity>().eq("id", entity.getId()));
        if (count != 1) {
            throw new EmosException("更新角色失败");
        }
    }
}

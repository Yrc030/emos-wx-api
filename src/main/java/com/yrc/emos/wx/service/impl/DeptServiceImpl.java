package com.yrc.emos.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yrc.emos.wx.exception.EmosException;
import com.yrc.emos.wx.pojo.entity.DeptEntity;
import com.yrc.emos.wx.mapper.DeptMapper;
import com.yrc.emos.wx.service.DeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, DeptEntity> implements DeptService {

    @Override
    public List<DeptEntity> getAllDept() {
        return list(new QueryWrapper<DeptEntity>().orderByAsc("id"));
    }

    @Override
    @Transactional
    public void saveDept(DeptEntity entity) {
        if (!save(entity)) {
            throw new EmosException("保存失败");
        }
    }

    @Override
    @Transactional
    public void updateDept(DeptEntity entity) {
        if (!update(entity, new QueryWrapper<DeptEntity>().eq("id", entity.getId()))) {
            throw new EmosException("保存失败");
        }
    }

    @Override
    @Transactional
    public void deleteDept(Integer id) {
        if (!removeById(id)) {
            throw new EmosException("删除失败");
        }
    }


}

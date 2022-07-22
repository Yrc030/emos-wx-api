package com.yrc.emos.wx.service;

import com.yrc.emos.wx.pojo.entity.DeptEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
public interface DeptService extends IService<DeptEntity> {

    List<DeptEntity> getAllDept();

    void saveDept(DeptEntity entity);

    void updateDept(DeptEntity entity);

    void deleteDept(Integer id);
}

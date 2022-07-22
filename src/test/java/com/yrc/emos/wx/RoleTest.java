package com.yrc.emos.wx;

import com.yrc.emos.wx.pojo.result.RolePermissionResult;
import com.yrc.emos.wx.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * Description:
 * User: joker
 * Date: 2022-07-13-11:23
 * Time: 11:23
 */

public class RoleTest extends EmosWxApiApplicationTests{

    @Autowired
    private RoleService service;


    /**
     * 用于测试:
     */
    @Test
    public void test(){
        Collection<RolePermissionResult> roleOwnPermission = service.getRoleOwnPermission(1);


        System.out.println("roleOwnPermission = " + roleOwnPermission);
    }
}

package com.tencent.backstage.modules.system.dao;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseMapper;
import com.tencent.backstage.modules.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
public interface RoleDao  extends BaseMapper<Role> {
    List<Role> findByUserId(String userid);
    List<Role> findList(Role role);
    Role findByName(String name);
    void deleteRolePermissions(String roleid);
    void updateRolePermissions(@Param(value = "roleid") String roleid, @Param(value = "permissionid") String permissionid);
    void deleteRoleMenus(String roleid);
    void updateRoleMenus(@Param(value = "roleid") String roleid, @Param(value = "menuid") String menuid);
    List<Role> findByMenuId(String menuid);
    void removeRoleMenu(@Param(value = "roleid") String roleid, @Param(value = "menuid") String menuid);

    @Override
    List<Role> findWithPage(Page page , @Param(Constants.WRAPPER) Wrapper wrapper);
}

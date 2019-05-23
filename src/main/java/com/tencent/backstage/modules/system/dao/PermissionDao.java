package com.tencent.backstage.modules.system.dao;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseMapper;
import com.tencent.backstage.modules.system.entity.Permission;
import com.tencent.backstage.modules.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

public interface PermissionDao extends BaseMapper<Permission> {
    Set<Permission> findByRoles(@Param(value = "roles") Set<Role> roles);
    Set<Permission> findByRoleId(String roleid);
    Permission findByName(String name);

    List<Permission> findByPid(String pid);

    @Override
    List<Permission> findWithPage(Page page , @Param(Constants.WRAPPER) Wrapper wrapper);
    List<Permission> findAll(@Param(Constants.WRAPPER) Wrapper wrapper);
}
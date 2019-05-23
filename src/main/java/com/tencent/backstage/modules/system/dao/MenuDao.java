package com.tencent.backstage.modules.system.dao;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseMapper;
import com.tencent.backstage.modules.system.entity.Menu;
import com.tencent.backstage.modules.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/1
 * Time:18:26
 */
public interface MenuDao extends BaseMapper<Menu> {
    List<Menu> findByRoles(List<Role> roles);

    List<Menu> findByPid(String pid);

    List<Menu> findByRoles_IdOrderBySortAsc(String roleid);

    Menu findByName(String name);

    @Override
    List<Menu> findWithPage(Page page , @Param(Constants.WRAPPER) Wrapper wrapper);
    List<Menu> findAll(@Param(Constants.WRAPPER) Wrapper wrapper);
}

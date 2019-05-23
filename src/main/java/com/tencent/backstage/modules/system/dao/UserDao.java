package com.tencent.backstage.modules.system.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseMapper;
import com.tencent.backstage.modules.system.entity.Role;
import com.tencent.backstage.modules.system.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;
public interface UserDao extends BaseMapper<User> {
    User loadUserByUsername(String username);
    Set<Role> getRolesByUserId(String id);
    User findByEmail(String email);
    void updatePass(@Param(value = "username") String username, @Param(value = "pass") String pass);
    void updateAvatar(@Param(value = "username") String username, @Param(value = "url") String url);
    void updateEmail(@Param(value = "username") String username, @Param(value = "email") String email);
    void addUserRoleBatch(Map map);
    void deleteUserRoleByUserId(String userid);

    @Override
    List<User> findWithPage(Page page , @Param(Constants.WRAPPER) Wrapper wrapper);
}
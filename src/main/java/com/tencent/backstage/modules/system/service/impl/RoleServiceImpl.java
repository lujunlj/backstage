package com.tencent.backstage.modules.system.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencent.backstage.common.base.BaseServiceImpl;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.exception.EntityExistException;
import com.tencent.backstage.common.utils.Constants;
import com.tencent.backstage.common.utils.PageUtil;
import com.tencent.backstage.common.utils.ValidationUtil;
import com.tencent.backstage.modules.models.dto.RoleDto;
import com.tencent.backstage.modules.models.mapper.RoleDtoAndEntityMapper;
import com.tencent.backstage.modules.system.dao.RoleDao;
import com.tencent.backstage.modules.system.entity.Menu;
import com.tencent.backstage.modules.system.entity.Permission;
import com.tencent.backstage.modules.system.entity.Role;
import com.tencent.backstage.modules.system.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl extends BaseServiceImpl<RoleDao,Role> implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RoleDtoAndEntityMapper roleDtoAndEntityMapper;
    @Override
    public List<Role> findByUserId(String userid) {
        return roleDao.findByUserId(userid);
    }

    @Override
    public RoleDto findById(String uuid) {
        Optional<Role> role = Optional.ofNullable(roleDao.selectById(uuid));
        ValidationUtil.isNull(role,"Role","uuid",uuid);
        return roleDtoAndEntityMapper.toDto(role.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleDto create(Role resources) {
        if(roleDao.findByName(resources.getName()) != null){
            throw new EntityExistException(Role.class,"username",resources.getName());
        }
        if(super.save(resources)){
            return roleDtoAndEntityMapper.toDto(resources);
        }else{
            throw new BadRequestException(Constants.Save.ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Role resources) {
        Optional<Role> optionalRole = Optional.ofNullable(roleDao.selectById(resources.getUuid()));
        ValidationUtil.isNull(optionalRole,"Role","uuid",resources.getUuid());
        Role role = optionalRole.get();
        Role role1 = roleDao.findByName(resources.getName());
        if(role1 != null && !role1.getUuid().equals(role.getUuid())){
            throw new EntityExistException(Role.class,"username",resources.getName());
        }
        super.updateById(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(Role resources, RoleDto roleDTO) {
        Role role = roleDtoAndEntityMapper.toEntity(roleDTO);
//        role.setPermissions(resources.getPermissions());
        if(StringUtils.isNotBlank(role.getUuid())){
            roleDao.deleteRolePermissions(role.getUuid());
            for(Permission permission : resources.getPermissions()){
                if(StringUtils.isNotBlank(permission.getUuid())){
                    roleDao.updateRolePermissions(role.getUuid(),permission.getUuid());
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Role resources, RoleDto roleDTO) {
        Role role = roleDtoAndEntityMapper.toEntity(roleDTO);
//        role.setMenus(resources.getMenus());
        if(StringUtils.isNotBlank(role.getUuid())){
            roleDao.deleteRoleMenus(role.getUuid());
            for(Menu menu : resources.getMenus()){
                if(StringUtils.isNotBlank(menu.getUuid())){
                    roleDao.updateRoleMenus(role.getUuid(),menu.getUuid());
                }
            }
        }
    }

    @Override
    public void untiedMenu(Menu menu) {
        List<Role> roles = roleDao.findByMenuId(menu.getUuid());
        for (Role role : roles) {
            roleDao.removeRoleMenu(role.getUuid(),menu.getUuid());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String uuid) {
        roleDao.deleteById(uuid);
    }

    @Override
    public Object queryAll(RoleDto roleDto) {
        return roleDao.findList(roleDtoAndEntityMapper.toEntity(roleDto));
    }

    @Override
    public Object queryAll(String name, Page<Role> pageInfo) {
        Role role = new Role();
        role.setName(name);
        pageInfo = super.findWithPage(pageInfo, Wrappers.<Role>lambdaQuery()
                                      .eq(StringUtils.isNotBlank(role.getName()),Role::getName,role.getName())
                                        .orderByDesc(Role::getCreateTime));
        return PageUtil.toPage(roleDtoAndEntityMapper.toDto(pageInfo.getRecords()),pageInfo.getRecords().size());
    }

}

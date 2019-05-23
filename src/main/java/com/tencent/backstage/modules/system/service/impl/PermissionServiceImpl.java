package com.tencent.backstage.modules.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tencent.backstage.common.base.BaseServiceImpl;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.exception.EntityExistException;
import com.tencent.backstage.common.utils.Constants;
import com.tencent.backstage.common.utils.StringUtils;
import com.tencent.backstage.common.utils.ValidationUtil;
import com.tencent.backstage.modules.models.dto.PermissionDto;
import com.tencent.backstage.modules.models.mapper.PermissionDtoAndEntityMapper;
import com.tencent.backstage.modules.system.dao.PermissionDao;
import com.tencent.backstage.modules.system.entity.Permission;
import com.tencent.backstage.modules.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/30
 * Time:18:03
 */
@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionDao,Permission> implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private PermissionDtoAndEntityMapper permissionDtoAndEntityMapper;

    @Override
    public PermissionDto findById(String uuid) {
        Optional<Permission> permission = Optional.ofNullable(permissionDao.selectById(uuid));
        ValidationUtil.isNull(permission,"Permission","uuid",uuid);
        return permissionDtoAndEntityMapper.toDto(permission.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PermissionDto create(Permission resources) {
        if(permissionDao.findByName(resources.getName()) != null){
            throw new EntityExistException(Permission.class,"name",resources.getName());
        }
        return permissionDtoAndEntityMapper.toDto(super.saveNew(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Permission resources) {
        Optional<Permission> optionalPermission =Optional.ofNullable(permissionDao.selectById(resources.getUuid()));
        if(resources.getUuid().equals(resources.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        ValidationUtil.isNull(optionalPermission,"Permission","uuid",resources.getUuid());
        Permission permission = optionalPermission.get();
        Permission permission1 = permissionDao.findByName(resources.getName());
        if(permission1 != null && !permission1.getUuid().equals(permission.getUuid())){
            throw new EntityExistException(Permission.class,"name",resources.getName());
        }

        permission.setName(resources.getName());
        permission.setAlias(resources.getAlias());
        permission.setPid(resources.getPid());
        super.save(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String uuid) {
        List<Permission> permissionList = permissionDao.findByPid(uuid);
        for (Permission permission : permissionList) {
            super.deletePhysicsById(permission.getUuid());
        }
        super.deletePhysicsById(uuid);
    }

    @Override
    public Object getPermissionTree(List<Permission> permissions) {
        List<Map<String,Object>> list = new LinkedList<>();
        permissions.forEach(permission -> {
                    if (permission!=null){
                        List<Permission> permissionList = permissionDao.findByPid(permission.getUuid());
                        Map<String,Object> map = new HashMap<>();
                        map.put("id",permission.getUuid());
                        map.put("label",permission.getAlias());
                        if(permissionList!=null && permissionList.size()!=0){
                            map.put("childrens",getPermissionTree(permissionList));
                        }
                        list.add(map);
                    }
                }
        );
        return list;
    }

    @Override
    public List<Permission> findByPid(String pid) {
        return permissionDao.findByPid(pid);
    }

    @Override
    public Object buildTree(List<PermissionDto> permissionDTOS) {

        List<PermissionDto> trees = new ArrayList<PermissionDto>();

        for (PermissionDto permissionDTO : permissionDTOS) {

            if (Constants.Permission.rootid.equals(permissionDTO.getPid().toString())) {
                trees.add(permissionDTO);
            }

            for (PermissionDto it : permissionDTOS) {
                if (it.getPid().equals(permissionDTO.getUuid())) {
                    if (permissionDTO.getChildren() == null) {
                        permissionDTO.setChildren(new ArrayList<PermissionDto>());
                    }
                    permissionDTO.getChildren().add(it);
                }
            }
        }
        Integer totalElements = permissionDTOS!=null?permissionDTOS.size():0;
        Map map = new HashMap();
        map.put("content",trees.size() == 0?permissionDTOS:trees);
        map.put("totalElements",totalElements);
        return map;
    }

    @Override
    public List queryAll(String name) {
        Permission permission = new Permission();
        permission.setName(name);
        List<Permission> permissionList = permissionDao.findAll(Wrappers.<Permission>lambdaQuery()
                .eq(StringUtils.isNotBlank(permission.getName()),Permission::getName,permission.getName())
                .orderByDesc(Permission::getCreateTime));
        return permissionDtoAndEntityMapper.toDto(permissionList);
    }
}

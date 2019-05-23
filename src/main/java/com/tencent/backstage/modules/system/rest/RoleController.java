package com.tencent.backstage.modules.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.annotation.Log;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.utils.StringUtils;
import com.tencent.backstage.modules.models.dto.RoleDto;
import com.tencent.backstage.modules.system.entity.Role;
import com.tencent.backstage.modules.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/4
 * Time:13:56
 */
@RestController
@RequestMapping("api")
public class RoleController {

    @Autowired
    private RoleService roleService;


    private static final String ENTITY_NAME = "role";

    @GetMapping(value = "/roles/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_SELECT')")
    public ResponseEntity getRoles(@PathVariable String uuid){
        return new ResponseEntity(roleService.findById(uuid), HttpStatus.OK);
    }

    /**
     * 返回全部的角色，新增用户时下拉选择
     * @return
     */
    @GetMapping(value = "/roles/all")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','USER_ALL','USER_CREATE','USER_EDIT')")
    public ResponseEntity getAll(){
        return new ResponseEntity(roleService.queryAll(new RoleDto()),HttpStatus.OK);
    }

    @Log("查询角色")
    @GetMapping(value = "/roles")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_SELECT')")
    public ResponseEntity getRoles(@RequestParam(required = false) String name,  Page<Role> pageInfo){
        return new ResponseEntity(roleService.queryAll(name,pageInfo),HttpStatus.OK);
    }

    @Log("新增角色")
    @PostMapping(value = "/roles")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_CREATE')")
    public ResponseEntity create(@Validated @RequestBody Role resources){
        if (StringUtils.isNotBlank(resources.getUuid())) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity(roleService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改角色")
    @PutMapping(value = "/roles")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    public ResponseEntity update(@Validated @RequestBody Role resources){
        roleService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("修改角色权限")
    @PutMapping(value = "/roles/permission")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    public ResponseEntity updatePermission(@RequestBody Role resources){
        roleService.updatePermission(resources,roleService.findById(resources.getUuid()));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("修改角色菜单")
    @PutMapping(value = "/roles/menu")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    public ResponseEntity updateMenu(@RequestBody Role resources){
        roleService.updateMenu(resources,roleService.findById(resources.getUuid()));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除角色")
    @DeleteMapping(value = "/roles/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_DELETE')")
    public ResponseEntity delete(@PathVariable String uuid){
        roleService.delete(uuid);
        return new ResponseEntity(HttpStatus.OK);
    }

}

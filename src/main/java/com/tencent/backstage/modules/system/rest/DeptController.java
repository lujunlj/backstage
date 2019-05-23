package com.tencent.backstage.modules.system.rest;

import com.tencent.backstage.common.annotation.Log;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.utils.StringUtils;
import com.tencent.backstage.config.system.DataScope;
import com.tencent.backstage.modules.models.dto.DeptDto;
import com.tencent.backstage.modules.system.entity.Dept;
import com.tencent.backstage.modules.system.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/4
 * Time:13:36
 */
@RestController
@RequestMapping("api")
public class DeptController {

    @Autowired
    private DeptService deptService;


    @Autowired
    private DataScope dataScope;

    private static final String ENTITY_NAME = "dept";

    @Log("查询部门")
    @GetMapping(value = "/dept")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_SELECT','DEPT_ALL','DEPT_SELECT')")
    public ResponseEntity getDepts(DeptDto resources){
        // 数据权限
        Set<String> deptIds = dataScope.getDeptIds();
        List<DeptDto> deptDTOS = deptService.queryAll(resources, deptIds);
        return new ResponseEntity(deptService.buildTree(deptDTOS),HttpStatus.OK);
    }

    @Log("新增部门")
    @PostMapping(value = "/dept")
    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody Dept resources){
        if (StringUtils.isNotBlank(resources.getUuid())) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity(deptService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改部门")
    @PutMapping(value = "/dept")
    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_EDIT')")
    public ResponseEntity update(@Validated @RequestBody Dept resources){
        deptService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除部门")
    @DeleteMapping(value = "/dept/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_DELETE')")
    public ResponseEntity delete(@PathVariable String uuid){
        deptService.delete(uuid);
        return new ResponseEntity(HttpStatus.OK);
    }


}

package com.tencent.backstage.modules.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.annotation.Log;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.config.system.DataScope;
import com.tencent.backstage.modules.system.entity.Job;
import com.tencent.backstage.modules.system.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/4
 * Time:13:56
 */
@RestController
@RequestMapping("api")
public class JobController {
    @Autowired
    private JobService jobService;

    @Autowired
    private DataScope dataScope;

    private static final String ENTITY_NAME = "job";

    @Log("查询岗位")
    @GetMapping(value = "/job")
    @PreAuthorize("hasAnyRole('ADMIN','USERJOB_ALL','USERJOB_SELECT','USER_ALL','USER_SELECT')")
    public ResponseEntity getJobs(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) String deptId,
                                  @RequestParam(required = false) Boolean enabled,
                                  Page<Job> pageInfo){
        // 数据权限
        Set<String> deptIds = dataScope.getDeptIds();
        return new ResponseEntity(jobService.queryAll(name, enabled , deptIds, deptId, pageInfo), HttpStatus.OK);
    }

    @Log("新增岗位")
    @PostMapping(value = "/job")
    @PreAuthorize("hasAnyRole('ADMIN','USERJOB_ALL','USERJOB_CREATE')")
    public ResponseEntity create(@Validated @RequestBody Job resources){
        if (resources.getUuid() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity(jobService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改岗位")
    @PutMapping(value = "/job")
    @PreAuthorize("hasAnyRole('ADMIN','USERJOB_ALL','USERJOB_EDIT')")
    public ResponseEntity update(@Validated @RequestBody Job resources){
        jobService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除岗位")
    @DeleteMapping(value = "/job/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN','USERJOB_ALL','USERJOB_DELETE')")
    public ResponseEntity delete(@PathVariable String uuid){
        jobService.delete(uuid);
        return new ResponseEntity(HttpStatus.OK);
    }
}

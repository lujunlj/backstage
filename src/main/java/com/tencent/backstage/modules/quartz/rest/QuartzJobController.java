package com.tencent.backstage.modules.quartz.rest;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.annotation.Log;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.modules.quartz.entity.QuartzJob;
import com.tencent.backstage.modules.quartz.entity.QuartzLog;
import com.tencent.backstage.modules.quartz.service.QuartzJobService;
import com.tencent.backstage.modules.quartz.service.QuartzLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lujun
 * @since 2019-05-25
 */
@RestController
@RequestMapping("api")
public class QuartzJobController {
    private static final String ENTITY_NAME = "quartzJob";

    @Autowired
    private QuartzJobService quartzJobService;

    @Autowired
    private QuartzLogService quartzLogService;

    @Log("查询定时任务")
    @GetMapping(value = "/jobs")
    @PreAuthorize("hasAnyRole('ADMIN','JOB_ALL','JOB_SELECT')")
    public ResponseEntity getJobs(QuartzJob resources, Page<QuartzJob> pageable){
        return new ResponseEntity(quartzJobService.queryAll(resources,pageable), HttpStatus.OK);
    }

    /**
     * 查询定时任务日志
     * @param resources
     * @param pageable
     * @return
     */
    @Log("查询定时任务日志")
    @GetMapping(value = "/jobLogs")
    @PreAuthorize("hasAnyRole('ADMIN','JOB_ALL','JOB_SELECT')")
    public ResponseEntity getJobLogs(QuartzLog resources, Page<QuartzLog> pageable){
        return new ResponseEntity(quartzLogService.queryAll(resources,pageable), HttpStatus.OK);
    }

    @Log("新增定时任务")
    @PostMapping(value = "/jobs")
    @PreAuthorize("hasAnyRole('ADMIN','JOB_ALL','JOB_CREATE')")
    public ResponseEntity create(@Validated @RequestBody QuartzJob resources){
        if (resources.getUuid() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity(quartzJobService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改定时任务")
    @PutMapping(value = "/jobs")
    @PreAuthorize("hasAnyRole('ADMIN','JOB_ALL','JOB_EDIT')")
    public ResponseEntity update(@Validated @RequestBody QuartzJob resources){
        quartzJobService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("更改定时任务状态")
    @PutMapping(value = "/jobs/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN','JOB_ALL','JOB_EDIT')")
    public ResponseEntity updateIsPause(@PathVariable String uuid){
        quartzJobService.updateIsPause(quartzJobService.findById(uuid));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("执行定时任务")
    @PutMapping(value = "/jobs/exec/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN','JOB_ALL','JOB_EDIT')")
    public ResponseEntity execution(@PathVariable String uuid){
        quartzJobService.execution(quartzJobService.findById(uuid));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除定时任务")
    @DeleteMapping(value = "/jobs/{uuid}")
    @PreAuthorize("hasAnyRole('ADMIN','JOB_ALL','JOB_DELETE')")
    public ResponseEntity delete(@PathVariable String uuid){
        quartzJobService.delete(quartzJobService.findById(uuid));
        return new ResponseEntity(HttpStatus.OK);
    }
}

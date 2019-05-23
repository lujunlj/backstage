package com.tencent.backstage.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.modules.models.dto.LogDto;
import com.tencent.backstage.modules.system.entity.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/23
 * Time:15:20
 */
@Service
public interface LogService  {
    /**
     * 新增日志
     * @param joinPoint
     * @param logDto
     */
    @Async
    void save(ProceedingJoinPoint joinPoint, LogDto logDto);
    Object queryAll(Log log, Page<Log> pageInfo);
}

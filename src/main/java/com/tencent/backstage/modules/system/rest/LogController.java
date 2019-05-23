package com.tencent.backstage.modules.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.utils.SecurityContextHolder;
import com.tencent.backstage.modules.system.entity.Log;
import com.tencent.backstage.modules.system.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/24
 * Time:22:17
 */
@RestController
@RequestMapping("/api")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping(value = "/logs")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity getLogs(Log log, Page<Log> pageInfo){
        log.setLogType("INFO");
        return new ResponseEntity(logService.queryAll(log,pageInfo), HttpStatus.OK);
    }

    @GetMapping(value = "/logs/user")
    public ResponseEntity getUserLogs(Log log, Page<Log> pageInfo){
        log.setLogType("INFO");
        log.setUsername(SecurityContextHolder.getUserDetails().getUsername());
        return new ResponseEntity(logService.queryAll(log,pageInfo), HttpStatus.OK);
    }

    @GetMapping(value = "/logs/error")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity getErrorLogs(Log log, Page<Log> pageInfo){
        log.setLogType("ERROR");
        return new ResponseEntity(logService.queryAll(log,pageInfo), HttpStatus.OK);
    }


}

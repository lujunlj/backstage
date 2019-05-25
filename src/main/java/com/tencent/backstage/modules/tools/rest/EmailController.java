package com.tencent.backstage.modules.tools.rest;

import com.tencent.backstage.common.annotation.Log;
import com.tencent.backstage.modules.models.vo.EmailVo;
import com.tencent.backstage.modules.tools.entity.EmailConfig;
import com.tencent.backstage.modules.tools.service.EmailService;
import lombok.extern.slf4j.Slf4j;
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
 * Time:21:42
 */
@Slf4j
@RestController
@RequestMapping("api")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PreAuthorize("hasAnyRole('ADMIN','Email_ALL','Email_SELECT')")
    @GetMapping(value = "/email")
    public ResponseEntity get(){
        return new ResponseEntity(emailService.find(), HttpStatus.OK);
    }

    @Log("配置邮件")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_UPDATE')")
    @PutMapping(value = "/email")
    public ResponseEntity emailConfig(@Validated @RequestBody EmailConfig emailConfig){
        emailService.update(emailConfig,emailService.find());
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("发送邮件")
    @PreAuthorize("hasAnyRole('ADMIN','PICTURE_ALL','PICTURE_SEND')")
    @PostMapping(value = "/email")
    public ResponseEntity send(@Validated @RequestBody EmailVo emailVo) throws Exception {
        log.warn("REST request to send Email : {}" ,emailVo);
        emailService.send(emailVo,emailService.find());
        return new ResponseEntity(HttpStatus.OK);
    }
}

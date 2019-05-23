package com.tencent.backstage.modules.system.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseServiceImpl;
import com.tencent.backstage.common.utils.*;
import com.tencent.backstage.modules.models.dto.LogDto;
import com.tencent.backstage.modules.models.mapper.LogDtoAndEntityMapper;
import com.tencent.backstage.modules.system.dao.LogDao;
import com.tencent.backstage.modules.system.entity.Log;
import com.tencent.backstage.modules.system.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/23
 * Time:15:23
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogServiceImpl extends BaseServiceImpl<LogDao,Log> implements LogService {

    @Autowired
    private LogDao logDao;

    @Autowired
    private LogDtoAndEntityMapper logDtoAndEntityMapper;

    private final String LOGINPATH = "login";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ProceedingJoinPoint joinPoint, LogDto logDto){

        // 获取request
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.tencent.backstage.common.annotation.Log aopLog = method.getAnnotation(com.tencent.backstage.common.annotation.Log.class);


        if (logDto == null) {
            logDto = new LogDto();
        }

        // 描述
        logDto.setDescription(aopLog.value());

        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName()+"."+signature.getName()+"()";
        String params = "{";
        //参数值
        Object[] argValues = joinPoint.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        // 用户名
        String username = "";

        if(argValues != null){
            for (int i = 0; i < argValues.length; i++) {
                params += " " + argNames[i] + ": " + argValues[i];
            }
        }else{
            argValues = new Object[0];
        }

        // 获取IP地址
        logDto.setRequestIp(StringUtils.getIP(request));
        logDto.setMethod(methodName);
        logDto.setParams(params + " }");
        logDto.setCreateTime(LocalDateTime.now());
        logDto.setUuid(IdGen.uuid());
        try {
            if(!LOGINPATH.equals(signature.getName())){
                UserDetails userDetails = SecurityContextHolder.getUserDetails();
                username = userDetails.getUsername();
            } else {
                    JSONObject jsonObject = new JSONObject(argValues[0]);
                    username = jsonObject.get("username").toString();
            }
            logDto.setUsername(username);
            Log log = logDtoAndEntityMapper.toEntity(logDto);
            logDao.insert(log);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public Object queryAll(Log log, Page<Log> pageInfo) {
        pageInfo = super.findWithPage(pageInfo, Wrappers.<Log>lambdaQuery()
                                      .eq(StringUtils.isNotBlank(log.getLogType()),Log::getLogType,log.getLogType())
                                      .like(StringUtils.isNotBlank(log.getUsername()),Log::getUsername,log.getUsername())
                                        .orderByDesc(Log::getCreateTime));
        return PageUtil.toPage(pageInfo);
    }
}

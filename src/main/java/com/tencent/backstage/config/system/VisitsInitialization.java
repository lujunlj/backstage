package com.tencent.backstage.config.system;

import com.tencent.backstage.modules.monitor.service.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/5
 * Time:17:52
 */
@Slf4j
@Component
public class VisitsInitialization implements ApplicationRunner {

    @Autowired
    private VisitService visitService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("-------------------------------------- 初始化站点统计，如果存在今日统计则跳过 --------------------------------------");
        visitService.save();
        log.info("-------------------------------------- 初始化站点统计完成 -------------------------------------------------------");
    }
}

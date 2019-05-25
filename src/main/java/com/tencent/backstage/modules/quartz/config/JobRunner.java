package com.tencent.backstage.modules.quartz.config;

import com.tencent.backstage.modules.quartz.dao.QuartzJobDao;
import com.tencent.backstage.modules.quartz.entity.QuartzJob;
import com.tencent.backstage.modules.quartz.service.QuartzJobService;
import com.tencent.backstage.modules.quartz.utils.QuartzManage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jie
 * @date 2019-01-07
 */
@Slf4j
@Component
public class JobRunner implements ApplicationRunner {

    @Autowired
    private QuartzJobDao quartzJobDao;

    @Autowired
    private QuartzManage quartzManage;

    /**
     * 项目启动时重新激活启用的定时任务
     * @param applicationArguments
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments applicationArguments){
        log.info("--------------------注入定时任务---------------------");
        List<QuartzJob> quartzJobs = quartzJobDao.findByIsPauseIsFalse();
        quartzJobs.forEach(quartzJob -> {
            quartzManage.addJob(quartzJob);
        });
        log.info("--------------------定时任务注入完成---------------------");
    }
}

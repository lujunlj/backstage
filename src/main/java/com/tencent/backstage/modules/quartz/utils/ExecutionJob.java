package com.tencent.backstage.modules.quartz.utils;

import com.tencent.backstage.common.utils.SpringContextHolder;
import com.tencent.backstage.common.utils.ThrowableUtil;
import com.tencent.backstage.modules.quartz.entity.QuartzJob;
import com.tencent.backstage.modules.quartz.entity.QuartzLog;
import com.tencent.backstage.modules.quartz.service.QuartzJobService;
import com.tencent.backstage.modules.quartz.service.QuartzLogService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 参考人人开源，https://gitee.com/renrenio/renren-security
 * @author
 * @date 2019-01-07
 */
@Slf4j
@Async
public class ExecutionJob extends QuartzJobBean {


    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void executeInternal(JobExecutionContext context) {
        QuartzJob quartzJob = (QuartzJob) context.getMergedJobDataMap().get(QuartzJob.JOB_KEY);
        // 获取spring bean
        QuartzLogService quartzLogService = SpringContextHolder.getBean("quartzLogService");
        QuartzJobService quartzJobService = SpringContextHolder.getBean("quartzJobService");
        QuartzManage quartzManage = SpringContextHolder.getBean("quartzManage");

        QuartzLog quartzLog = new QuartzLog();
        quartzLog.setJobName(quartzJob.getJobName());
        quartzLog.setBaenName(quartzJob.getBeanName());
        quartzLog.setMethodName(quartzJob.getMethodName());
        quartzLog.setParams(quartzJob.getParams());
        long startTime = System.currentTimeMillis();
        quartzLog.setCronExpression(quartzJob.getCronExpression());
        try {
            // 执行任务
            log.info("任务准备执行，任务名称：{}", quartzJob.getJobName());
            QuartzRunnable task = new QuartzRunnable(quartzJob.getBeanName(), quartzJob.getMethodName(),
                    quartzJob.getParams());
            Future<?> future = executorService.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            quartzLog.setTime(times);
            // 任务状态
            quartzLog.setIsSuccess(true);
            log.info("任务执行完毕，任务名称：{} 总共耗时：{} 毫秒", quartzJob.getJobName(), times);
        } catch (Exception e) {
            log.error("任务执行失败，任务名称：{}" + quartzJob.getJobName(), e);
            long times = System.currentTimeMillis() - startTime;
            quartzLog.setTime(times);
            // 任务状态 0：成功 1：失败
            quartzLog.setIsSuccess(false);
            quartzLog.setExceptionDetail(ThrowableUtil.getStackTrace(e));
            //出错就暂停任务
            quartzManage.pauseJob(quartzJob);
            //更新状态
            quartzJobService.updateIsPause(quartzJob);
        } finally {
            quartzLogService.save(quartzLog);
        }
    }
}

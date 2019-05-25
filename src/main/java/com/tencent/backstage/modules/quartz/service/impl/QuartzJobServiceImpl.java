package com.tencent.backstage.modules.quartz.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseServiceImpl;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.utils.Constants;
import com.tencent.backstage.common.utils.PageUtil;
import com.tencent.backstage.common.utils.StringUtils;
import com.tencent.backstage.common.utils.ValidationUtil;
import com.tencent.backstage.modules.quartz.entity.QuartzJob;
import com.tencent.backstage.modules.quartz.dao.QuartzJobDao;
import com.tencent.backstage.modules.quartz.service.QuartzJobService;
import com.tencent.backstage.modules.quartz.utils.QuartzManage;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lujun
 * @since 2019-05-25
 */
@Service(value = "quartzJobService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class QuartzJobServiceImpl extends BaseServiceImpl<QuartzJobDao, QuartzJob> implements QuartzJobService {

    @Autowired
    private QuartzJobDao quartzJobDao;

    @Autowired
    private QuartzManage quartzManage;

    @Override
    public QuartzJob findById(String uuid) {
        Optional<QuartzJob> quartzJob = Optional.of(quartzJobDao.selectById(uuid));
        ValidationUtil.isNull(quartzJob,"QuartzJob","id",uuid);
        return quartzJob.get();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QuartzJob create(QuartzJob resources) {
        if (!CronExpression.isValidExpression(resources.getCronExpression())){
            throw new BadRequestException("cron表达式格式错误");
        }
        quartzJobDao.insert(resources);
        quartzManage.addJob(resources);
        return resources;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(QuartzJob resources) {
        if(resources.getUuid().equals("1")){
            throw new BadRequestException("该任务不可操作");
        }
        if (!CronExpression.isValidExpression(resources.getCronExpression())){
            throw new BadRequestException("cron表达式格式错误");
        }
        quartzJobDao.updateById(resources);
        quartzManage.updateJobCron(resources);
    }

    @Override
    public void updateIsPause(QuartzJob quartzJob) {
        if("1".equals(quartzJob.getUuid())){
            throw new BadRequestException("该任务不可操作");
        }
        if (quartzJob.getIsPause()) {
            quartzManage.resumeJob(quartzJob);
            quartzJob.setIsPause(false);
        } else {
            quartzManage.pauseJob(quartzJob);
            quartzJob.setIsPause(true);
        }
        quartzJobDao.updateById(quartzJob);
    }

    @Override
    public void execution(QuartzJob quartzJob) {
        if("1".equals(quartzJob.getUuid())){
            throw new BadRequestException("该任务不可操作");
        }
        quartzManage.runAJobNow(quartzJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(QuartzJob quartzJob) {
        if("1".equals(quartzJob.getUuid())){
            throw new BadRequestException("该任务不可操作");
        }
        quartzManage.deleteJob(quartzJob);
        quartzJobDao.deleteById(quartzJob);
    }

    @Override
    public Object queryAll(QuartzJob resource, Page<QuartzJob> pageinfo) {
        pageinfo = super.findWithPage(pageinfo, Wrappers.<QuartzJob>lambdaQuery()
                                        .eq(QuartzJob::getDelFlag, Constants.NODELETE)
                                        .like(StringUtils.isNotBlank(resource.getJobName()),QuartzJob::getJobName,resource.getJobName())
                                        .orderByDesc(QuartzJob::getCreateTime));
        return PageUtil.toPage(pageinfo);
    }
}

package com.tencent.backstage.modules.quartz.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseServiceImpl;
import com.tencent.backstage.common.utils.Constants;
import com.tencent.backstage.common.utils.PageUtil;
import com.tencent.backstage.modules.quartz.entity.QuartzJob;
import com.tencent.backstage.modules.quartz.entity.QuartzLog;
import com.tencent.backstage.modules.quartz.dao.QuartzLogDao;
import com.tencent.backstage.modules.quartz.service.QuartzLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lujun
 * @since 2019-05-25
 */
@Service
public class QuartzLogServiceImpl extends BaseServiceImpl<QuartzLogDao, QuartzLog> implements QuartzLogService {

    @Override
    public Object queryAll(QuartzLog resource, Page<QuartzLog> pageinfo) {
        pageinfo = super.findWithPage(pageinfo, Wrappers.<QuartzLog>lambdaQuery()
                                     .eq(QuartzLog::getDelFlag, Constants.NODELETE)
                                     .eq(resource.getIsSuccess()!=null,QuartzLog::getIsSuccess,resource.getIsSuccess())
                                     .like(StringUtils.isNotBlank(resource.getJobName()),QuartzLog::getJobName,resource.getJobName())
                                     .orderByDesc(QuartzLog::getCreateTime)
                                     );
        return PageUtil.toPage(pageinfo);
    }
}

package com.tencent.backstage.modules.quartz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tencent.backstage.modules.quartz.entity.QuartzLog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lujun
 * @since 2019-05-25
 */
public interface QuartzLogService extends IService<QuartzLog> {
    Object queryAll(QuartzLog resource, Page<QuartzLog> pageinfo);
}

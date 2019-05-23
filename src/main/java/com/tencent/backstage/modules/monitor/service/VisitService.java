package com.tencent.backstage.modules.monitor.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.tencent.backstage.modules.monitor.entity.Visit;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/24
 * Time:17:52
 */
public interface VisitService extends IService<Visit> {

    /**
     * 提供给定时任务，每天0点执行
     */
    Visit save();

    /**
     * 新增记录
     * @param request
     */
    @Async
    void count(HttpServletRequest request);

    /**
     * 获取数据
     * @return
     */
    Object get();

    /**
     * getChartData
     * @return
     */
    Object getChartData();
}

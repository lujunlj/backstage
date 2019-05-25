package com.tencent.backstage.modules.quartz.task;

import com.tencent.backstage.modules.monitor.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2018-12-25
 */
@Component
public class VisitsTask {

    @Autowired
    private VisitService visitService;

    public void run(){
        visitService.save();
    }
}

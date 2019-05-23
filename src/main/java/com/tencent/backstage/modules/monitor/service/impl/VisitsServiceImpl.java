package com.tencent.backstage.modules.monitor.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencent.backstage.common.utils.StringUtils;
import com.tencent.backstage.modules.monitor.dao.VisitDao;
import com.tencent.backstage.modules.monitor.entity.Visit;
import com.tencent.backstage.modules.monitor.service.VisitService;
import com.tencent.backstage.modules.system.dao.LogDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/24
 * Time:17:52
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class VisitsServiceImpl extends ServiceImpl<VisitDao, Visit> implements VisitService {

    @Autowired
    private VisitDao visitDao;

    @Autowired
    private LogDao logDao;

    @Override
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public Visit save() {
        LocalDate localDate = LocalDate.now();
        Visit visit = visitDao.findByDate(localDate.toString());
        if(visit == null){
            visit = new Visit();
            visit.setWeekDay(StringUtils.getWeekDay());
            visit.setPvCounts(1L);
            visit.setIpCounts(1L);
            visit.setDate(localDate.toString());
            visit.setCreateTime(LocalDateTime.now());
            super.save(visit);
        }
        return visit;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void count(HttpServletRequest request) {
        Visit visit = this.save();
        LocalDate localDate = LocalDate.now();
        visit.setPvCounts(visit.getPvCounts()+1);
        long ipCounts = logDao.findIp(localDate.toString(), localDate.plusDays(1).toString());
        visit.setIpCounts(ipCounts);
        super.updateById(visit);
    }

    @Override
    public Object get() {
        Map map = new HashMap();
        LocalDate localDate = LocalDate.now();
        Visit visit = visitDao.findByDate(localDate.toString());
        List<Visit> list = visitDao.findAllVisits(localDate.minusDays(6).toString(),localDate.plusDays(1).toString());

        long recentVisits = 0, recentIp = 0;
        for (Visit data : list) {
            recentVisits += data.getPvCounts();
            recentIp += data.getIpCounts();
        }
        map.put("newVisits",visit.getPvCounts());
        map.put("newIp",visit.getIpCounts());
        map.put("recentVisits",recentVisits);
        map.put("recentIp",recentIp);
        return map;
    }

    @Override
    public Object getChartData() {
        Map map = new HashMap();
        LocalDate localDate = LocalDate.now();
        List<Visit> list = visitDao.findAllVisits(localDate.minusDays(6).toString(),localDate.plusDays(1).toString());
        map.put("weekDays",list.stream().map(Visit::getWeekDay).collect(Collectors.toList()));
        map.put("visitsData",list.stream().map(Visit::getPvCounts).collect(Collectors.toList()));
        map.put("ipData",list.stream().map(Visit::getIpCounts).collect(Collectors.toList()));
        return map;
    }
}

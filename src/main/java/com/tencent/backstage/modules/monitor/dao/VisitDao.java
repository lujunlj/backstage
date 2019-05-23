package com.tencent.backstage.modules.monitor.dao;


import com.tencent.backstage.common.base.BaseMapper;
import com.tencent.backstage.modules.monitor.entity.Visit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VisitDao extends BaseMapper<Visit> {
    /**
     * findByDate
     * @param date
     * @return
     */
    Visit findByDate(String date);

    /**
     * 获得一个时间段的记录
     * @param date1
     * @param date2
     * @return
     */
    List<Visit> findAllVisits(@Param(value = "date1") String date1, @Param(value = "date2") String date2);
}
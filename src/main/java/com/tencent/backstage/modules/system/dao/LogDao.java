package com.tencent.backstage.modules.system.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseMapper;
import com.tencent.backstage.modules.system.entity.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/23
 * Time:15:27
 */
public interface LogDao extends BaseMapper<Log> {
    /**
     * 获取一个时间段的IP记录
     * @param date1
     * @param date2
     * @return
     */
    Long findIp(@Param(value = "date1") String date1, @Param(value = "date2") String date2);

    @Override
    List<Log> findWithPage(Page page , @Param(Constants.WRAPPER) Wrapper wrapper);
}

package com.tencent.backstage.modules.system.dao;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseMapper;
import com.tencent.backstage.modules.system.entity.Job;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/3
 * Time:20:03
 */
public interface JobDao extends BaseMapper<Job> {

    @Override
    Job selectById(String uuid);

    @Override
    List<Job> findWithPage(Page page , @Param(Constants.WRAPPER) Wrapper wrapper);
}

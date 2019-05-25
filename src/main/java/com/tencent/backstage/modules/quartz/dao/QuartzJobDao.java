package com.tencent.backstage.modules.quartz.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseMapper;
import com.tencent.backstage.modules.quartz.entity.QuartzJob;
import com.tencent.backstage.modules.system.entity.Dept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lujun
 * @since 2019-05-25
 */
public interface QuartzJobDao extends BaseMapper<QuartzJob> {

    List<QuartzJob> findByIsPauseIsFalse();

    @Override
    List<QuartzJob> findWithPage(Page page , @Param(Constants.WRAPPER) Wrapper wrapper);
}

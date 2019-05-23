package com.tencent.backstage.modules.tools.dao;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.common.base.BaseMapper;
import com.tencent.backstage.modules.tools.entity.Picture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/4
 * Time:10:37
 */
public interface PictureDao extends BaseMapper<Picture> {
    @Override
    List<Picture> findWithPage(Page page , @Param(Constants.WRAPPER) Wrapper wrapper);
}

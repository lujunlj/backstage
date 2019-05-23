package com.tencent.backstage.common.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/21
 * Time:16:50
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
    T selectById(String uuid);
    List<T> findWithPage(Page<T> page, Wrapper<T> wrapper);
    List<T> findListByMap(Page<T> page,@Param("map") Map map);
    void deletePhysicsById(String uuid);
}

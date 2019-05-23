package com.tencent.backstage.common.base;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencent.backstage.common.exception.BadRequestException;
import com.tencent.backstage.common.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/21
 * Time:16:44
 */
public abstract  class BaseServiceImpl<M extends BaseMapper<T> , T> extends ServiceImpl<M , T> {

    @Autowired
    private M m;

    public Page<T> findWithPage(Page<T> page, Wrapper wrapper){
//        PageHelper.startPage(pageInfo.getPageNum(),pageInfo.getPageSize());
          List<T> tList =  m.findWithPage(page,wrapper);
//        PageHelper.clearPage();
//        pageInfo.setList(tList);
         page.setRecords(tList);
        return page;
    }

    public Page<T> findWithPageByMap(Page<T> page, Map map){
//        PageHelper.startPage(pageInfo.getPageNum(),pageInfo.getPageSize());
        List<T> tList =  m.findListByMap(page,map);
//        PageHelper.clearPage();
//        pageInfo.setList(tList);
        page.setRecords(tList);
        return page;
    }

    public T saveNew(T entity){
        if(entity!=null && super.save(entity)){
            return entity;
        }else{
            throw new BadRequestException(Constants.Save.ERROR);
        }
    }

    public void deletePhysicsById(String uuid){
        if(StringUtils.isNoneBlank(uuid)){
            m.deletePhysicsById(uuid);
        }
    }

}

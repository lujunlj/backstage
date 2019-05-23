package com.tencent.backstage.config.orm;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/20
 * Time:12:00
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * @param metaObject
     * @return void
     * @description: mybatis plus 插入时自动填充字段
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        LocalDateTime now = LocalDateTime.now();
        this.setFieldValByName("createTime",now,metaObject);
        this.setFieldValByName("updateTime",now,metaObject);
        this.setFieldValByName("delFlag","0",metaObject);
        this.setFieldValByName("version", 1L, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        LocalDateTime now = LocalDateTime.now();
        this.setFieldValByName("updateTime",now,metaObject);
    }

}

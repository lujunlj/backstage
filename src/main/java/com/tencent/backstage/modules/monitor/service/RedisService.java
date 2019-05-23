package com.tencent.backstage.modules.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tencent.backstage.modules.models.vo.RedisVo;
import com.tencent.backstage.modules.monitor.entity.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/4
 * Time:21:35
 */
public interface RedisService  {
    /**
     * findById
     * @param key
     * @return
     */
    public Page findByKey(String key, Pageable pageable);

    /**
     * create
     * @param redisVo
     */
    public void save(RedisVo redisVo);

    /**
     * delete
     * @param key
     */
    public void delete(String key);

    /**
     * 清空所有缓存
     */
    public void flushdb();
}

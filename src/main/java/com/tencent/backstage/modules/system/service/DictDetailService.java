package com.tencent.backstage.modules.system.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.modules.models.dto.DictDetailDto;
import com.tencent.backstage.modules.system.entity.DictDetail;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/3
 * Time:14:05
 */
@CacheConfig(cacheNames = "dictDetail")
public interface DictDetailService {

    /**
     * findById
     * @param uuid
     * @return
     */
    @Cacheable(key = "#p0")
    DictDetailDto findById(String uuid);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    DictDetailDto create(DictDetail resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(DictDetail resources);

    /**
     * delete
     * @param uuid
     */
    @CacheEvict(allEntries = true)
    void delete(String uuid);

    @Cacheable(keyGenerator = "keyGenerator")
    public Object queryAll(DictDetailDto dictDetailDto, Page<DictDetail> pageInfo);
}
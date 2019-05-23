package com.tencent.backstage.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.modules.models.dto.DictDto;
import com.tencent.backstage.modules.system.entity.Dict;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/3
 * Time:14:05
 */
@CacheConfig(cacheNames = "dict")
public interface DictService {

    /**
     * findById
     * @param uuid
     * @return
     */
    @Cacheable(key = "#p0")
    DictDto findById(String uuid);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    DictDto create(Dict resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(Dict resources);

    /**
     * delete
     * @param uuid
     */
    @CacheEvict(allEntries = true)
    void delete(String uuid);

    @Cacheable(keyGenerator = "keyGenerator")
    public Object queryAll(DictDto dictDto, Page<Dict> pageInfo);
}
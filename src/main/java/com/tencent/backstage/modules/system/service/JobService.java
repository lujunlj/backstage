package com.tencent.backstage.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.modules.models.dto.JobDto;
import com.tencent.backstage.modules.system.entity.Job;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Set;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/3
 * Time:19:57
 */
@CacheConfig(cacheNames = "job")
public interface JobService {

    /**
     * findById
     * @param uuid
     * @return
     */
    @Cacheable(key = "#p0")
    JobDto findById(String uuid);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    JobDto create(Job resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(Job resources);

    /**
     * delete
     * @param uuid
     */
    @CacheEvict(allEntries = true)
    void delete(String uuid);

    @Cacheable(keyGenerator = "keyGenerator")
    public Object queryAll(String name, Boolean enabled, Set<String> deptIds, String deptId, Page<Job> pageInfo);
}
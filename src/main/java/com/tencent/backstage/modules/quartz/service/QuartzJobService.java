package com.tencent.backstage.modules.quartz.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.modules.quartz.entity.QuartzJob;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lujun
 * @since 2019-05-25
 */
@CacheConfig(cacheNames="quartzJob")
public interface QuartzJobService  {

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    QuartzJob create(QuartzJob resources);

    /**
     * update
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    void update(QuartzJob resources);

    /**
     * del
     * @param quartzJob
     */
    @CacheEvict(allEntries = true)
    void delete(QuartzJob quartzJob);

    /**
     * findById
     * @param uuid
     * @return
     */
    @Cacheable(key = "#p0")
    QuartzJob findById(String uuid);

    /**
     * 更改定时任务状态
     * @param quartzJob
     */
    @CacheEvict(allEntries = true)
    void updateIsPause(QuartzJob quartzJob);

    /**
     * 立即执行定时任务
     * @param quartzJob
     */
    void execution(QuartzJob quartzJob);

    @Cacheable(keyGenerator = "keyGenerator")
    Object queryAll(QuartzJob resource, Page<QuartzJob> pageinfo);
}

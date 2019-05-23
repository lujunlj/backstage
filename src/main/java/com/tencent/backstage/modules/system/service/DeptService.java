package com.tencent.backstage.modules.system.service;


import com.tencent.backstage.modules.models.dto.DeptDto;
import com.tencent.backstage.modules.system.entity.Dept;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Set;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/3
 * Time:19:58
 */

@CacheConfig(cacheNames = "dept")
public interface DeptService {

    /**
     * findById
     * @param uuid
     * @return
     */
    @Cacheable(key = "#p0")
    DeptDto findById(String uuid);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    DeptDto create(Dept resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(Dept resources);

    /**
     * delete
     * @param uuid
     */
    @CacheEvict(allEntries = true)
    void delete(String uuid);

    /**
     * buildTree
     * @param deptDTOS
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    Object buildTree(List<DeptDto> deptDTOS);

    /**
     * findByPid
     * @param pid
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    List<Dept> findByPid(String pid);

    @Cacheable(keyGenerator = "keyGenerator")
    public List<DeptDto> queryAll(DeptDto dept, Set<String> deptIds);
}

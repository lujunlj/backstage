package com.tencent.backstage.modules.system.service;

import com.tencent.backstage.modules.models.dto.PermissionDto;
import com.tencent.backstage.modules.system.entity.Permission;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/30
 * Time:18:02
 */
@CacheConfig(cacheNames = "permission")
public interface PermissionService {

    /**
     * get
     *
     * @param uuid
     * @return
     */
    @Cacheable(key = "#p0")
    PermissionDto findById(String uuid);

    /**
     * create
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    PermissionDto create(Permission resources);

    /**
     * update
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(Permission resources);

    /**
     * delete
     *
     * @param uuid
     */
    @CacheEvict(allEntries = true)
    void delete(String uuid);

    /**
     * permission tree
     *
     * @return
     */
    @Cacheable(key = "'tree'")
    Object getPermissionTree(List<Permission> permissions);

    /**
     * findByPid
     *
     * @param pid
     * @return
     */
    @Cacheable(key = "'pid:'+#p0")
    List<Permission> findByPid(String pid);

    /**
     * build Tree
     *
     * @param permissionDTOS
     * @return
     */
    @Cacheable(keyGenerator = "keyGenerator")
    Object buildTree(List<PermissionDto> permissionDTOS);

    @Cacheable(key = "'queryAll:'+#p0")
    public List queryAll(String name);
}
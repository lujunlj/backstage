package com.tencent.backstage.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.modules.models.dto.RoleDto;
import com.tencent.backstage.modules.system.entity.Menu;
import com.tencent.backstage.modules.system.entity.Role;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;


/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/19
 * Time:11:03
 */
@CacheConfig(cacheNames = "role")
public interface RoleService {
    /**
     * key的名称如有修改，请同步修改 UserServiceImpl 中的 update 方法
     * findByUserId
     * @param userid
     * @return
     */
    @Cacheable(key = "'findByUserId:' + #p0")
    List<Role> findByUserId(String userid);

    /**
     * get
     * @param uuid
     * @return
     */
    @Cacheable(key = "#p0")
    RoleDto findById(String uuid);

    /**
     * create
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    RoleDto create(Role resources);

    /**
     * update
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(Role resources);
    /**
     * delete
     * @param uuid
     */
    @CacheEvict(allEntries = true)
    void delete(String uuid);
    /**
     * updatePermission
     * @param resources
     * @param roleDTO
     */
    @CacheEvict(allEntries = true)
    void updatePermission(Role resources, RoleDto roleDTO);

    /**
     * updateMenu
     * @param resources
     * @param roleDTO
     */
    @CacheEvict(allEntries = true)
    void updateMenu(Role resources, RoleDto roleDTO);

    @CacheEvict(allEntries = true)
    void untiedMenu(Menu menu);

    @Cacheable(keyGenerator = "keyGenerator")
    public Object queryAll(RoleDto roleDto);

    @Cacheable(keyGenerator = "keyGenerator")
    public Object queryAll(String name, Page<Role> rolePageInfo);

}

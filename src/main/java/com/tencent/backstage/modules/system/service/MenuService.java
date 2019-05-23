package com.tencent.backstage.modules.system.service;

import com.tencent.backstage.modules.models.dto.MenuDto;
import com.tencent.backstage.modules.system.entity.Menu;
import com.tencent.backstage.modules.system.entity.Role;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/1
 * Time:18:28
 */
@CacheConfig(cacheNames = "menu")
public interface MenuService {

    /**
     * get
     *
     * @param uuid
     * @return
     */
    @Cacheable(key = "#p0")
    MenuDto findById(String uuid);

    /**
     * create
     *
     * @param resources
     * @return
     */
    @CacheEvict(allEntries = true)
    MenuDto create(Menu resources);

    /**
     * update
     *
     * @param resources
     */
    @CacheEvict(allEntries = true)
    void update(Menu resources);

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
    Object getMenuTree(List<Menu> menus);

    /**
     * findByPid
     *
     * @param pid
     * @return
     */
    @Cacheable(key = "'pid:'+#p0")
    List<Menu> findByPid(String pid);

    /**
     * build Tree
     *
     * @param menuDTOS
     * @return
     */
    Map buildTree(List<MenuDto> menuDTOS);

    /**
     * findByRoles
     *
     * @param roles
     * @return
     */
    List<MenuDto> findByRoles(List<Role> roles);

    /**
     * buildMenus
     *
     * @param byRoles
     * @return
     */
    Object buildMenus(List<MenuDto> byRoles);

    Menu findOne(String uuid);

    /**
     * 不分页
     */
    @Cacheable(key = "'queryAll:'+#p0")
    public List queryAll(String name);
}
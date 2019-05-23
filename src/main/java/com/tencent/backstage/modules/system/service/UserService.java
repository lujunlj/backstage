package com.tencent.backstage.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.backstage.modules.models.dto.UserDto;
import com.tencent.backstage.modules.system.entity.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Set;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/24
 * Time:17:52
 */
@CacheConfig(cacheNames = "user")
public interface UserService  {
    
    @Cacheable(key = "#p0")
    UserDto findById(String userid);

    @CacheEvict(allEntries = true)
    UserDto create(User resources);

    @CacheEvict(allEntries = true)
    void update(User resources);

    @CacheEvict(allEntries = true)
    void delete(String userid);

    @Cacheable(key = "'loadUserByUsername:'+#p0")
    User findByUserName(String username);

    @Cacheable(keyGenerator = "keyGenerator")
    public Object queryAll(UserDto userDto, Set<String> deptIds, Page<User> pageInfo);

    /**
     * 修改密码
     * @param username
     * @param encryptPassword
     */
    @CacheEvict(allEntries = true)
    void updatePass(String username, String encryptPassword);

    /**
     * 修改头像
     * @param username
     * @param url
     */
    @CacheEvict(allEntries = true)
    void updateAvatar(String username, String url);

    /**
     * 修改邮箱
     * @param username
     * @param email
     */
    @CacheEvict(allEntries = true)
    void updateEmail(String username, String email);

}

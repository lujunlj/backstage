package com.tencent.backstage.modules.security.service;

import com.tencent.backstage.modules.system.dao.PermissionDao;
import com.tencent.backstage.modules.system.dao.RoleDao;
import com.tencent.backstage.modules.system.dao.UserDao;
import com.tencent.backstage.modules.system.entity.Role;
import com.tencent.backstage.modules.system.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@CacheConfig(cacheNames = "role")
public class JwtPermissionService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * key的名称如有修改，请同步修改 UserServiceImpl 中的 update 方法
     * @param user
     * @return
     */
    @Cacheable(key = "'loadPermissionByUser:' + #p0.username")
    public Collection<GrantedAuthority> mapToGrantedAuthorities(User user) {

        log.info("--------------------loadPermissionByUser:" + user.getUsername() + "---------------------");

//        Set<Role> Role = userDao.getRolesByUserId(user.getUuid());
//
//        Set<Permission> permissions = new HashSet<>();
//
//        if(roles != null && roles.size()>0){
//            permissions.addAll(permissionDao.findByRoles(roles));
//        }
        List<Role> roles = roleDao.findByUserId(user.getUuid());
        return roles.stream().flatMap(role -> role.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());

//        return permissions.stream()
//                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
//                .collect(Collectors.toList());
    }
}

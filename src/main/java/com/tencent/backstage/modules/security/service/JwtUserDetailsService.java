package com.tencent.backstage.modules.security.service;


import com.tencent.backstage.common.exception.LoginErrorException;
import com.tencent.backstage.modules.system.entity.Dept;
import com.tencent.backstage.modules.system.entity.Job;
import com.tencent.backstage.modules.system.entity.JwtUser;
import com.tencent.backstage.modules.system.entity.User;
import com.tencent.backstage.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("jwtUserDetailsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtPermissionService jwtPermissionService;

    @Override
    public UserDetails loadUserByUsername(String username){
        User user = userService.findByUserName(username);
        if (user == null) {
//            throw new EntityNotFoundException(User.class, "name", username);
            throw new LoginErrorException("用户名或密码错误!");
        } else {
            return createJwtUser(user);
        }
    }

    public UserDetails createJwtUser(User user){
        return new JwtUser(
                user.getUuid(),
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getAvatar(),
                user.getPhone(),
                user.getTelephone(),
                user.getEmail(),
                Optional.ofNullable(user.getDept()).map(Dept::getName).orElse(null),
                Optional.ofNullable(user.getJob()).map(Job::getName).orElse(null),
                jwtPermissionService.mapToGrantedAuthorities(user),
                user.getEnabled(),
                user.getCreateTime(),
                user.getLastPasswordResetTime()
        );
    }
}

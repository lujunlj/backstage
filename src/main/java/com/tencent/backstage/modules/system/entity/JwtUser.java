package com.tencent.backstage.modules.system.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @date 2018-11-23
 */
@Getter
@AllArgsConstructor
public class JwtUser implements UserDetails {
    private static final long serialVersionUID = 1L;
    @JSONField(serialize = false)
    private final String uuid;
    private final String name;
    private final String username;
    @JSONField(serialize = false)
    private final String password;
    private final String avatar;
    private final String phone;
    private final String telephone;

    private final String email;

    private final String dept;

    private final String job;

    @JSONField(serialize = false)
    private final Collection<GrantedAuthority> authorities;

    private final boolean enabled;
    private LocalDateTime createTime;

    @JSONField(serialize = false)
    private final LocalDateTime lastPasswordResetDate;

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Collection getRoles() {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }


}

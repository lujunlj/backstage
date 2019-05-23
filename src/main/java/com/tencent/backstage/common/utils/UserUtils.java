package com.tencent.backstage.common.utils;

import org.springframework.security.core.userdetails.UserDetails;

public class UserUtils {
    public static UserDetails getCurrentUser() {
        return SecurityContextHolder.getUserDetails();
    }

}

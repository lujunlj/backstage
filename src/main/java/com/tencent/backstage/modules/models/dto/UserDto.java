package com.tencent.backstage.modules.models.dto;


import com.tencent.backstage.modules.system.entity.Dept;
import com.tencent.backstage.modules.system.entity.Job;
import com.tencent.backstage.modules.system.entity.Role;
import com.tencent.backstage.modules.system.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/24
 * Time:21:34
 */
@Data
@NoArgsConstructor
public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String uuid;
    private String name;
    private String phone;
    private String telephone;
    private String address;
    private Boolean enabled;
    private String username;
    private String password;
    private String remark;
    @Pattern(regexp = "([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}",message = "格式错误")
    private String email;
    private String avatar;
    private LocalDateTime createTime;
    private LocalDateTime lastPasswordResetTime;
    private Set<Role> roles ;
    private Set<String> deptids ;
    private String jobId;
    private String deptId;
    private Long version;
    private Dept dept;
    private Job job;

}

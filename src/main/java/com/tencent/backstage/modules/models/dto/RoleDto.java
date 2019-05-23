package com.tencent.backstage.modules.models.dto;

import com.tencent.backstage.modules.system.entity.Dept;
import com.tencent.backstage.modules.system.entity.Menu;
import com.tencent.backstage.modules.system.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/4
 * Time:14:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto implements Serializable{
    private static final long serialVersionUID = 1L;
    private String id;
    private String uuid;
    private LocalDateTime createTime;
    private String name;
    private String remark;
    private String dataScope;
    private Long version;
    private Set<Dept> depts;
    private Set<Permission> permissions;
    private Set<Menu> menus;
}

package com.tencent.backstage.modules.models.dto;

import com.tencent.backstage.modules.system.entity.Role;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/1
 * Time:18:27
 */
@Data
public class MenuDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String uuid;
    private LocalDateTime createTime;
    private Boolean iFrame;
    private String name;
    private String component;
    private String pid;
    private Long sort;
    private String icon;
    private String path;
    private Long version;
    private List<MenuDto> children;
    private Set<Role> roles;
}

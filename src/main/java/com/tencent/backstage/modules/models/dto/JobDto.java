package com.tencent.backstage.modules.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/3
 * Time:19:48
 */
@Data
@NoArgsConstructor
public class JobDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String uuid;
    private String name;
    private Boolean enabled;
    private LocalDateTime createTime;
    private Long sort;
    private String deptId;
    private String delFlag;
    private Long version;
    private DeptDto dept;
}

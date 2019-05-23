package com.tencent.backstage.modules.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/3
 * Time:19:47
 */
@Data
@NoArgsConstructor
public class DeptDto implements Serializable{
    private static final long serialVersionUID = 1L;
    private String id;
    private String uuid;
    private String name;
    private String pid;
    private LocalDateTime createTime;
    private Boolean enabled;
    private String delFlag;
    private Long version;
    private Set<String> deptids;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DeptDto> children;

    public String getLabel() {
        return name;
    }
}

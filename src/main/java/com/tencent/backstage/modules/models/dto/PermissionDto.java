package com.tencent.backstage.modules.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/30
 * Time:18:09
 */
@Data
@NoArgsConstructor
public class PermissionDto  implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String uuid;
    @NotBlank
    private String alias;
    private LocalDateTime createTime;
    @NotBlank
    private String name;
    @NotNull
    private String pid;
    private Long version;
    private List<PermissionDto> children;
}

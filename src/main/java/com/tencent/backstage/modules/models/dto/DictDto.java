package com.tencent.backstage.modules.models.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/3
 * Time:14:07
 */
@Data
public class DictDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String uuid;
    private String name;
    private String remark;
    private Long version;
}

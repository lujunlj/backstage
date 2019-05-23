package com.tencent.backstage.modules.models.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/3
 * Time:14:08
 */
@Data
public class DictDetailDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String uuid;
    private String label;
    private String value;
    private String sort = "999";
    private String dictId;
    private String dictName;
    private Long version;
}
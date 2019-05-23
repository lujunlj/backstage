package com.tencent.backstage.modules.models.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 构建前端路由时用到
 * @author lujun
 * @date 2019-05-01
 */
@Data
public class MenuVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;

    private String path;

    private String redirect;

    private String component;

    private Boolean alwaysShow;

    private MenuMetaVo meta;
    private List<MenuVo> children;
}

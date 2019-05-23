package com.tencent.backstage.modules.models.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lujun
 * @date 2019-05-01
 */
@Data
@AllArgsConstructor
public class MenuMetaVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;

    private String icon;
}

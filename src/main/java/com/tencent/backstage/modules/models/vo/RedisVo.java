package com.tencent.backstage.modules.models.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/4
 * Time:21:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank
    private String key;

    @NotBlank
    private String value;
}

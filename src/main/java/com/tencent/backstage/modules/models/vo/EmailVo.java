package com.tencent.backstage.modules.models.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/4
 * Time:12:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVo {
    /**
     * 收件人，支持多个收件人，用逗号分隔
     */
    @NotEmpty
    private List<String> tos;

    @NotBlank
    private String subject;

    @NotBlank
    private String content;
}

package com.tencent.backstage.modules.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/4
 * Time:10:39
 */
@Data
@NoArgsConstructor
public class PictureDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String uuid;
    private LocalDateTime createTime;
    private String delete;
    private String filename;
    private String height;
    private String size;
    private String url;
    private String username;
    private Long version;
}

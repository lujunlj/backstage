package com.tencent.backstage.modules.models.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/24
 * Time:21:34
 */
@Data
@NoArgsConstructor
public class LogDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String uuid;
    /**
     * 操作用户
     */
    private String username;

    /**
     * 描述
     */
    private String description;

    /**
     * 日志类型
     */
    private String logType;

    /**
     * 请求ip
     */
    private String requestIp;

    /**
     * 请求耗时
     */
    private Long time;

    /**
     * 异常详细
     */
    private String exceptionDetail;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;

    /**
     * 方法名
     */
    private String method;

    /**
     * 参数
     */
    private String params;

    private Long version;


    public LogDto(String logType, Long time) {
        this.logType = logType;
        this.time = time;
    }

}

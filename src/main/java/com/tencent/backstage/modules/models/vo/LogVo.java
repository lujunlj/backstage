package com.tencent.backstage.modules.models.vo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/24
 * Time:21:33
 */
@Data
public class LogVo implements Serializable {
    private static final long serialVersionUID = 1L;
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
    private Date createTime;

}

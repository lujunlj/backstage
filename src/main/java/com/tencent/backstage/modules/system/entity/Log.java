package com.tencent.backstage.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/4/23
 * Time:15:16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "sys_log",resultMap = "BaseResultMap")
@ApiModel(value="SysLog对象", description="日志表")
public class Log extends Model<Log> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;

    /**
     * 创建日期
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 描述
     */
    @TableField("description")
    private String description;
    /**
     * 异常详细
     */
    @TableField("exception_detail")
    private String exceptionDetail;
    /**
     * 日志类型
     */
    @TableField("log_type")
    private String logType;
    /**
     * 方法名
     */
    @TableField("method")
    private String method;
    /**
     * 参数
     */
    @TableField("params")
    private String params;
    /**
     * 请求ip
     */
    @TableField("request_ip")
    private String requestIp;
    /**
     * 请求耗时
     */
    @TableField("time")
    private Long time;
    /**
     * 操作用户
     */
    @TableField("username")
    private String username;


    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

}

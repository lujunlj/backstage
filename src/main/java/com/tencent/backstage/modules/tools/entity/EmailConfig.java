package com.tencent.backstage.modules.tools.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/5/4
 * Time:21:48
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "email_config",resultMap = "BaseResultMap")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="EmailConfig对象", description="邮箱配置表")
public class EmailConfig extends Model<EmailConfig> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;

    @ApiModelProperty(value = "收件人")
    @TableField("from_user")
    private String fromUser;

    @ApiModelProperty(value = "邮件服务器SMTP地址")
    @TableField("host")
    private String host;

    @ApiModelProperty(value = "密码")
    @TableField("pass")
    private String pass;

    @ApiModelProperty(value = "端口")
    @TableField("port")
    private String port;

    @ApiModelProperty(value = "发件者用户名，默认为发件人邮箱前缀")
    @TableField("user")
    private String user;

    @ApiModelProperty(value = "乐观锁版本标志")
    @TableField(value = "version", fill = FieldFill.INSERT)
    @Version
    private Long version;


    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }



}

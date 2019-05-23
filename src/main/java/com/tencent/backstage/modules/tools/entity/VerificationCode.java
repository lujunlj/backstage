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

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "verification_code",resultMap = "BaseResultMap")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="VerificationCode对象", description="验证码表")
public class VerificationCode extends Model<VerificationCode> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;

    @ApiModelProperty(value = "验证码")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "创建日期")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "状态：1有效、0过期")
    @TableField("status")
    private Boolean status;

    @ApiModelProperty(value = "验证码类型：email或者短信")
    @TableField("type")
    @NotBlank
    private String type;

    @ApiModelProperty(value = "接收邮箱或者手机号码")
    @TableField("value")
    @NotBlank
    private String value;

    @ApiModelProperty(value = "业务名称：如重置邮箱、重置密码等")
    @TableField("scenes")
    private String scenes;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

    public VerificationCode(String code, String scenes, @NotBlank String type, @NotBlank String value) {
        this.code = code;
        this.scenes = scenes;
        this.type = type;
        this.value = value;
    }

}
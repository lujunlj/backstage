package com.tencent.backstage.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Version;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/3/23
 * Time:19:26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "sys_user",resultMap = "BaseResultMap")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="SysUser对象", description="系统用户表")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "userID")
    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;

    @ApiModelProperty(value = "姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "手机号码")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "住宅电话")
    @TableField("telephone")
    private String telephone;

    @ApiModelProperty(value = "联系地址")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "状态：1启用、0禁用")
    @TableField("enabled")
    private Boolean enabled;

    @ApiModelProperty(value = "用户名")
    @TableField("username")
    private String username;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    @Pattern(regexp = "([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}",message = "格式错误")
    private String email;

    @ApiModelProperty(value = "创建日期")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改密码的日期")
    @TableField("last_password_reset_time")
    private LocalDateTime lastPasswordResetTime;

    @ApiModelProperty(value = "删除标志")
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    @TableLogic
    private String delFlag;

    @ApiModelProperty(value = "卡通图像")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty(value = "岗位id")
    @TableField("jobid")
    private String jobId;

    @ApiModelProperty(value = "部门id")
    @TableField("deptid")
    private String deptId;

    @ApiModelProperty(value = "乐观锁版本标志")
    @TableField(value = "version", fill = FieldFill.INSERT)
    @Version
    private Long version;

    @TableField(exist=false)
    private Set<Role> roles ;
    @TableField(exist=false)
    private Set<String> deptids ;
    @TableField(exist=false)
    private Dept dept;
    @TableField(exist=false)
    private Job job;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }


}
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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "sys_dept",resultMap = "BaseResultMap")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="SysDept对象", description="部门表")
public class Dept extends Model<Dept> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;

    @ApiModelProperty(value = "名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "上级部门")
    @TableField("pid")
    private String pid;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField("enabled")
    private Boolean enabled;

    @ApiModelProperty(value = "删除标志位")
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    @TableLogic
    private String delFlag;

    @ApiModelProperty(value = "乐观锁版本标志")
    @TableField(value = "version", fill = FieldFill.INSERT)
    @Version
    private Long version;


    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
    @TableField(exist=false)
    private Set<String> deptids;
}
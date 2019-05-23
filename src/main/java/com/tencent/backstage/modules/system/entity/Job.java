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

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "sys_job",resultMap = "BaseResultMap")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="SysJob对象", description="岗位表")
public class Job extends Model<Job> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;

    @TableField("name")
    private String name;

    @TableField("enabled")
    private Boolean enabled;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField("sort")
    private Long sort;

    @TableField("dept_id")
    private String deptId;

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

}
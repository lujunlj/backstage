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
import java.util.Objects;
import java.util.Set;

/**
 * Created with IDEA
 * author: lujun
 * Date:2019/3/23
 * Time:19:26
 */
@Data
@Accessors(chain = true)
@TableName(value = "sys_role",resultMap = "BaseResultMap")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="SysRole对象", description="角色表")
public class Role extends Model<Role> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;

    @ApiModelProperty(value = "创建日期")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    @TableField("data_scope")
    private String dataScope;

    @ApiModelProperty(value = "乐观锁版本标志")
    @TableField(value = "version", fill = FieldFill.INSERT)
    @Version
    private Long version;

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

    @TableField(exist=false)
    private Set<Dept> depts;
    @TableField(exist=false)
    private Set<Permission> permissions;
    @TableField(exist=false)
    private Set<Menu> menus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(uuid, role.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}

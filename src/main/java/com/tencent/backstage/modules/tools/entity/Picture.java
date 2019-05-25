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
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "picture",resultMap = "BaseResultMap")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="Picture对象", description="头像表")
public class Picture extends Model<Picture> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;

    @ApiModelProperty(value = "上传日期")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "删除图片的URL")
    @TableField("`delete`")
    private String delete;

    @ApiModelProperty(value = "图片名称")
    @TableField("filename")
    private String filename;

    @ApiModelProperty(value = "图片高度")
    @TableField("height")
    private String height;

    @ApiModelProperty(value = "图片大小")
    @TableField("size")
    private String size;

    @ApiModelProperty(value = "图片地址")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "用户名称")
    @TableField("username")
    private String username;

    @ApiModelProperty(value = "图片宽度")
    @TableField("width")
    private String width;

    @ApiModelProperty(value = "乐观锁版本标志")
    @TableField(value = "version", fill = FieldFill.INSERT)
    @Version
    private Long version;


    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }
}
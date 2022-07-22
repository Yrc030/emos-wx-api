package com.yrc.emos.wx.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Getter
@Setter
@TableName("tb_face_model")
@ApiModel(value = "FaceModelEntity对象", description = "")
public class FaceModelEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键值")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("用户人脸模型")
    private String faceModel;


}

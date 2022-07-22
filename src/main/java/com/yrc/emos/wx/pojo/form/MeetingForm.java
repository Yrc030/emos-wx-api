package com.yrc.emos.wx.pojo.form;

import com.yrc.emos.wx.constraints.annotations.IntListValue;
import com.yrc.emos.wx.constraints.group.InsertGroup;
import com.yrc.emos.wx.constraints.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@ApiModel("会议纪录form类")
public class MeetingForm {

    @ApiModelProperty("主键")
    //@TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "更新会议纪录时必须指定会议纪录id", groups = {UpdateGroup.class})
    @Null(message = "新增会议纪录时不能指定会议纪录id", groups = {InsertGroup.class})
    private Long id;

    @ApiModelProperty("UUID")
    @NotBlank(message = "更新会议纪录时必须指定会议纪录uuid", groups = {UpdateGroup.class})
    @Null(message = "新增会议纪录时不能指定会议纪录uuid", groups = {InsertGroup.class})
    private String uuid;

    @ApiModelProperty("会议标题")
    @NotBlank(message = "新增会议纪录时必须指定会议标题", groups = {InsertGroup.class})
    private String title;

    @ApiModelProperty("日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "新增会议纪录时必须指定会议日期", groups = {InsertGroup.class})
    private LocalDate date;

    @ApiModelProperty("开会地点")
    private String place;

    @ApiModelProperty("开始时间")
    //@TableField("`start`")
    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "新增会议纪录时必须指定会议开始时间", groups = {InsertGroup.class})
    private LocalTime start;

    @ApiModelProperty("结束时间")
    //@TableField("`end`")
    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "新增会议纪录时必须指定结束时间", groups = {InsertGroup.class})
    private LocalTime end;

    @ApiModelProperty("会议类型（1在线会议，2线下会议）")
    @NotNull(message = "新增会议纪录时必须指定会议类型", groups = {InsertGroup.class})
    @IntListValue(values = {1, 2}, message = "会议类型不正确", groups = {InsertGroup.class, UpdateGroup.class})
    private Integer type;

    @ApiModelProperty("会议成员")
    @NotEmpty(message = "新增会议纪录时必须指定会议成员", groups = {InsertGroup.class})
    private Integer[] members;

    @ApiModelProperty("会议内容")
    //@TableField("`desc`")
    @NotEmpty(message = "新增会议纪录时必须指定会议内容", groups = {InsertGroup.class})
    private String desc;

    @ApiModelProperty("工作流实例Id")
    @Null(message = "创建会议纪录时不能指定工作流实例id",groups = {InsertGroup.class})
    @NotBlank(message = "更新会议纪录时必须指定工作流实例id", groups = {UpdateGroup.class})
    private String instanceId;
}

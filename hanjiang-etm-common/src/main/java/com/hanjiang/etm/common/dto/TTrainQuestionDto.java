package com.hanjiang.etm.common.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;


/**
 * 描述：试卷模版DTO
 *
 * @author huxuehao
 **/
@Getter
@Setter
public class TTrainQuestionDto extends TQuestionTemplateDto {
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long trainQuestionUserTableId;
}

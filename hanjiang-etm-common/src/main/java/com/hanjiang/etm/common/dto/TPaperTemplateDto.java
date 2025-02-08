package com.hanjiang.etm.common.dto;

import com.hanjiang.etm.common.entity.TPaperTemplate;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

/**
 * 描述：试卷模版DTO
 *
 * @author huxuehao
 **/
@Getter
@Setter
public class TPaperTemplateDto extends TPaperTemplate {
    private LinkedList<Object> question;
}

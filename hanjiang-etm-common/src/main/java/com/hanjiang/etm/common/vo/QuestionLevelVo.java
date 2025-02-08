package com.hanjiang.etm.common.vo;

import lombok.Data;

import java.util.List;

/**
 * 描述：题目难度VO
 *
 * @author huxuehao
 **/
@Data
public class QuestionLevelVo {
    private List<QuestionLevel> one;
    private List<QuestionLevel> two;
    private List<QuestionLevel> three;
    private List<QuestionLevel> four;
    private List<QuestionLevel> five;
}

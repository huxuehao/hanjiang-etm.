package com.hanjiang.etm.common.vo;

import lombok.Data;

/**
 * 描述：导入试题Box
 *
 * @author huxuehao
 **/
@Data
public class ImportQuestionParserBoxVo {
    private ImportQuestionVo single;
    private ImportQuestionVo multiple;
    private ImportQuestionVo judge;
    private ImportQuestionVo completion;
    private ImportQuestionVo qa;
}

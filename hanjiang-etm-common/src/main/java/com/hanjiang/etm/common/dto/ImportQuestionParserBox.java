package com.hanjiang.etm.common.dto;

import lombok.Data;

/**
 * 描述：导入试题Box
 *
 * @author huxuehao
 **/
@Data
public class ImportQuestionParserBox {
    private ImportQuestion single;
    private ImportQuestion multiple;
    private ImportQuestion judge;
    private ImportQuestion completion;
    private ImportQuestion qa;
}

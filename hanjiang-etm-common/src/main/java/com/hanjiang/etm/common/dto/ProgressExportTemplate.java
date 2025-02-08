package com.hanjiang.etm.common.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * 描述：考试进展情况导出模版
 *
 * @author huxuehao
 **/
@Data
@HeadRowHeight(20)
@ContentRowHeight(20)
public class ProgressExportTemplate {
    @ExcelProperty("用户ID/手机号")
    @ColumnWidth(25)
    private String userId;

    @ExcelProperty("用户CODE/身份证号")
    @ColumnWidth(30)
    private String userCode;

    @ColumnWidth(20)
    @ExcelProperty("用户姓名")
    private String userName;

    @ExcelProperty("所属单位")
    @ColumnWidth(30)
    private String orgName;

    @ExcelProperty("答题情况")
    @ColumnWidth(15)
    private String completed;

    @ExcelProperty("完成时间")
    @ColumnWidth(25)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private String doTime;

    @ExcelProperty("总分")
    @ColumnWidth(10)
    private Integer totalScore;

    @ExcelProperty("合格分数")
    @ColumnWidth(15)
    private Integer qualifiedScore;

    @ExcelProperty("用户得分")
    @ColumnWidth(15)
    private Integer score;

    @ExcelProperty("是否合格")
    @ColumnWidth(15)
    private String qualified;

    @ExcelProperty("答题平台")
    @ColumnWidth(15)
    private String plat;
}

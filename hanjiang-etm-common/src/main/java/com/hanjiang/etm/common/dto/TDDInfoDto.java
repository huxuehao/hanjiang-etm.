package com.hanjiang.etm.common.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 钉钉信息采集
 */
@Getter
@Setter
public class TDDInfoDto {
    private static final long serialVersionUID = 1L;
    private String userId; // 手机号
    private String userName; // 真实姓名
    private String userCode; // 身份证号
    private String orgName; // 所属单位名称
    private Long examId; // 考试ID
}

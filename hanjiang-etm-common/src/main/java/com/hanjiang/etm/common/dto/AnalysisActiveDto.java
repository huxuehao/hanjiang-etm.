package com.hanjiang.etm.common.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@Getter
@Setter
@Accessors(chain = true)
public class AnalysisActiveDto {

    private String name;
    private List<String> activeDate;
    private List<String> aclist;

}

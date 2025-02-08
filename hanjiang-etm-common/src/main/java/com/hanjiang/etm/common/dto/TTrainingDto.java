package com.hanjiang.etm.common.dto;

import com.hanjiang.etm.common.entity.TTraining;
import com.hanjiang.etm.common.entity.TTrainingFile;
import com.hanjiang.etm.common.entity.TTrainingUser;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

/**
 * 描述：培训DTO
 *
 * @author huxuehao
 **/
@Getter
@Setter
public class TTrainingDto extends TTraining {
    LinkedList<Object> question;
    LinkedList<TTrainingFile> file;
    List<TTrainingUser> user;
}

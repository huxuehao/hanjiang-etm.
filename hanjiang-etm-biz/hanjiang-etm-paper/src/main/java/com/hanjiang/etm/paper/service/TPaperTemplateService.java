package com.hanjiang.etm.paper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.dto.TPaperTemplateDto;
import com.hanjiang.etm.common.entity.TPaperTemplate;

import java.util.List;

/**
 * 描述：试卷
 *
 * @author huxuehao
 **/
public interface TPaperTemplateService extends IService<TPaperTemplate> {
    boolean saveV2(TPaperTemplateDto paperTemplateDto);

    boolean removeBatchByIdsV2(List<Long> ids);
    boolean updateByIdV2(TPaperTemplateDto paperTemplateDto);
    TPaperTemplateDto getByIdV2(Long id);
}

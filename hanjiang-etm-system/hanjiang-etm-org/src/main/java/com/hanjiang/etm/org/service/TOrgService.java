package com.hanjiang.etm.org.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.dto.TOrganizationDto;
import com.hanjiang.etm.common.entity.TOrganization;

import java.util.List;

/**
 * 描述：组织机构
 *
 * @author huxuehao
 **/
public interface TOrgService extends IService<TOrganization> {

    List<TOrganizationDto> tree(TOrganization org);

    boolean deleteAllById(List<Long> ids);
}

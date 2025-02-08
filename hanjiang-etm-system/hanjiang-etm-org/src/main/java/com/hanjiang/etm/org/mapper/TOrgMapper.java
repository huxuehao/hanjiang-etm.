package com.hanjiang.etm.org.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hanjiang.etm.common.dto.TOrganizationDto;
import com.hanjiang.etm.common.entity.TOrganization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：组织机构
 *
 * @author huxuehao
 **/
@Mapper
public interface TOrgMapper extends BaseMapper<TOrganization> {
    List<TOrganizationDto> listV2(@Param("dbName")String dbName, @Param(Constants.WRAPPER) Wrapper<TOrganization> queryWrapper);
}

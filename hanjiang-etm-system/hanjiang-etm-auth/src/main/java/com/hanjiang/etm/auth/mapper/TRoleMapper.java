package com.hanjiang.etm.auth.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hanjiang.etm.common.dto.TRoleDto;
import com.hanjiang.etm.common.entity.TRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：角色
 *
 * @author huxuehao
 **/
@Mapper
public interface TRoleMapper extends BaseMapper<TRole> {
    List<TRoleDto> listV2(@Param("dbName")String dbName, @Param(Constants.WRAPPER) Wrapper<TRole> queryWrapper);
}

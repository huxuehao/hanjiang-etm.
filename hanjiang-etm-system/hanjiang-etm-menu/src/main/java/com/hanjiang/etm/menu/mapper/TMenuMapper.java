package com.hanjiang.etm.menu.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hanjiang.etm.common.dto.TMenuDto;
import com.hanjiang.etm.common.entity.TMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：菜单
 *
 * @author huxuehao
 **/
@Mapper
public interface TMenuMapper extends BaseMapper<TMenu> {
    List<TMenuDto> listV2(@Param("dbName")String dbName, @Param(Constants.WRAPPER) Wrapper<TMenu> queryWrapper);
}

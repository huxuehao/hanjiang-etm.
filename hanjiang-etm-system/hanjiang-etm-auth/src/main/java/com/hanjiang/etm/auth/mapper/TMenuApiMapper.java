package com.hanjiang.etm.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hanjiang.etm.common.dto.TMenuApiDto;
import com.hanjiang.etm.common.entity.TMenuApi;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：菜单接口
 *
 * @author huxuehao
 **/
@Mapper
public interface TMenuApiMapper extends BaseMapper<TMenuApi> {
    List<TMenuApiDto> listV2(
            @Param("menuDbName")String menuDbName,
            @Param("menuApiDbName")String menuApiDbName
    );
}

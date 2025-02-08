package com.hanjiang.etm.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hanjiang.etm.common.dto.TMenuButtonDto;
import com.hanjiang.etm.common.entity.TMenuButton;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：菜单按钮
 *
 * @author huxuehao
 **/
@Mapper
public interface TMenuButtonMapper extends BaseMapper<TMenuButton> {
    List<TMenuButtonDto> listV2(
            @Param("menuDbName")String menuDbName,
            @Param("menuButtonDbName")String menuButtonDbName
    );
}

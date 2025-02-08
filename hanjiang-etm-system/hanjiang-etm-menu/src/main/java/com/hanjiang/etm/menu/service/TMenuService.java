package com.hanjiang.etm.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.dto.TMenuDto;
import com.hanjiang.etm.common.entity.TMenu;

import java.util.List;

/**
 * 描述：菜单
 *
 * @author huxuehao
 **/
public interface TMenuService extends IService<TMenu> {
    List<TMenuDto> tree(TMenu menu);
    boolean deleteAllById(List<Long> ids);

}

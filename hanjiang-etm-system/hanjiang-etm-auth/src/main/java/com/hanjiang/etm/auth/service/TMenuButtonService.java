package com.hanjiang.etm.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.dto.TMenuButtonDto;
import com.hanjiang.etm.common.entity.TMenuButton;

import java.util.List;

/**
 * 描述：菜单按钮
 *
 * @author huxuehao
 **/
public interface TMenuButtonService extends IService<TMenuButton> {
    List<TMenuButtonDto> tree();
}

package com.hanjiang.etm.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.dto.TMenuApiDto;
import com.hanjiang.etm.common.entity.TMenuApi;

import java.util.List;

/**
 * 描述：菜单接口
 *
 * @author huxuehao
 **/
public interface TMenuApiService extends IService<TMenuApi> {
    List<TMenuApiDto> tree();
}

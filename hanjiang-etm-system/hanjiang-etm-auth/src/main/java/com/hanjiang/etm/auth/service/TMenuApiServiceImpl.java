package com.hanjiang.etm.auth.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.auth.mapper.TMenuApiMapper;
import com.hanjiang.etm.common.consts.DBConst;
import com.hanjiang.etm.common.dto.TMenuApiDto;
import com.hanjiang.etm.common.entity.TMenuApi;
import com.hanjiang.etm.common.util.tree.TreeUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：菜单接口
 *
 * @author huxuehao
 **/
@Service
public class TMenuApiServiceImpl extends ServiceImpl<TMenuApiMapper,TMenuApi> implements TMenuApiService {
    @Override
    public List<TMenuApiDto> tree() {
        List<TMenuApiDto> list = baseMapper.listV2(DBConst.T_MENU, DBConst.T_MENU_API);
        return TreeUtil.convertTree(list);
    }
}

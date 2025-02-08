package com.hanjiang.etm.auth.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.auth.mapper.TMenuButtonMapper;
import com.hanjiang.etm.common.consts.DBConst;
import com.hanjiang.etm.common.dto.TMenuButtonDto;
import com.hanjiang.etm.common.entity.TMenuButton;
import com.hanjiang.etm.common.util.tree.TreeUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：菜单按钮
 *
 * @author huxuehao
 **/
@Service
public class TMenuButtonServiceImpl extends ServiceImpl<TMenuButtonMapper, TMenuButton> implements TMenuButtonService {
    @Override
    public List<TMenuButtonDto> tree() {
        List<TMenuButtonDto> list = baseMapper.listV2(DBConst.T_MENU, DBConst.T_MENU_BUTTON);
        return TreeUtil.convertTree(list);
    }
}

package com.hanjiang.etm.menu.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.consts.DBConst;
import com.hanjiang.etm.common.dto.TMenuDto;
import com.hanjiang.etm.common.entity.TMenu;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.util.Func;
import com.hanjiang.etm.common.util.tree.TreeUtil;
import com.hanjiang.etm.menu.mapper.TMenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：菜单
 *
 * @author huxuehao
 **/
@Service
public class TMenuServiceImpl extends ServiceImpl<TMenuMapper, TMenu> implements TMenuService {

    @Override
    public List<TMenuDto> tree(TMenu menu) {
        List<TMenuDto> list = baseMapper.listV2(DBConst.T_MENU, MP.getQueryWrapper(menu));
        return TreeUtil.convertTree(list);
    }

    @Override
    @Transactional
    public boolean deleteAllById(List<Long> ids) {
        QueryWrapper<TMenu> qw = new QueryWrapper<>();
        qw.in("parent_id", ids);
        qw.eq("del_flag", 0);
        List<TMenu> menuList = list(qw);

        if (Func.isEmpty(menuList)) {
            return removeBatchByIds(ids);
        }

        // 有子级的ID集合
        List<Long> hasChildIds = menuList.stream().map(TMenu::getParentId).collect(Collectors.toList());
        // 没有子级的ID集合
        List<Long> noneChildIds = ids.stream().filter(id -> !hasChildIds.contains(id)).collect(Collectors.toList());
        removeBatchByIds(noneChildIds);

        return deleteAllById(hasChildIds);
    }

}

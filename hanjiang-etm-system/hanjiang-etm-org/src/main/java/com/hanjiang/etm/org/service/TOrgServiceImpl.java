package com.hanjiang.etm.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.consts.DBConst;
import com.hanjiang.etm.common.dto.TOrganizationDto;
import com.hanjiang.etm.common.entity.TOrganization;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.util.Func;
import com.hanjiang.etm.common.util.tree.TreeUtil;
import com.hanjiang.etm.org.mapper.TOrgMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：组织机构
 *
 * @author huxuehao
 **/
@Service
public class TOrgServiceImpl extends ServiceImpl<TOrgMapper, TOrganization> implements TOrgService {
    @Override
    public List<TOrganizationDto> tree(TOrganization org) {
        List<TOrganizationDto> list = baseMapper.listV2(DBConst.T_ORGANIZATION, MP.getQueryWrapper(org));
        return TreeUtil.convertTree(list);
    }

    @Override
    @Transactional
    public boolean deleteAllById(List<Long> ids) {
        QueryWrapper<TOrganization> qw = new QueryWrapper<>();
        qw.in("parent_id", ids);
        qw.eq("del_flag", 0);
        List<TOrganization> orgList = list(qw);

        if (Func.isEmpty(orgList)) {
            return removeBatchByIds(ids);
        }
        // 有子级的ID集合
        List<Long> hasChildIds = orgList.stream().map(TOrganization::getParentId).collect(Collectors.toList());
        // 没有子级的ID集合
        List<Long> noneChildIds = ids.stream().filter(id -> !hasChildIds.contains(id)).collect(Collectors.toList());
        removeBatchByIds(noneChildIds);

        return deleteAllById(hasChildIds);
    }

}

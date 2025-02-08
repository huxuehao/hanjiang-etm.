package com.hanjiang.etm.org.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TOrganization;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.org.service.TOrgService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：组织机构
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/org")
@MenuTag(code = "system_org")
public class TOrgController {
    private final TOrgService orgService;

    public TOrgController(TOrgService orgService) {
        this.orgService = orgService;
    }

    @PreAuthorize("@ps.hasPermission('post::org:add')")
    @PostMapping(value = "/add", name = "新增")
    public R<?> add(@RequestBody TOrganization body) {
        QueryWrapper<TOrganization> qw = new QueryWrapper<>();
        qw.eq("code", body.getCode());
        List<TOrganization> list = orgService.list(qw);
        if (list == null || list.isEmpty()) {
            return R.data(orgService.save(body));
        } else {
            return R.fail("组织编码已存在");
        }
    }

    @PreAuthorize("@ps.hasPermission('post::org:delete')")
    @PostMapping(value = "/delete", name = "删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        return R.data(orgService.deleteAllById(ids));
    }

    @PreAuthorize("@ps.hasPermission('post::org:update')")
    @PostMapping(value = "/update", name = "编辑")
    public R<?> update(@RequestBody TOrganization body) {
        QueryWrapper<TOrganization> qw = new QueryWrapper<>();
        qw.eq("code", body.getCode());
        qw.ne("id", body.getId());
        List<TOrganization> list = orgService.list(qw);
        if (list == null || list.isEmpty()) {
            if (body.getParentId() == null) {
                body.setParentId(0L);
            }
            return R.data(orgService.updateById(body));
        } else {
            return R.fail("组织编码已存在");
        }
    }

    @PreAuthorize("@ps.hasPermission('get::org:tree')")
    @GetMapping(value = "/tree", name = "树形列表")
    public R<?> tree(TOrganization org) {
        return R.data(orgService.tree(org));
    }

    @PreAuthorize("@ps.hasPermission('get::org:selectOne')")
    @GetMapping(value = "/selectOne", name = "根据ID唯一获取")
    public R<?> selectOne(@RequestParam Long id) {
        return R.data(orgService.getById(id));
    }
}

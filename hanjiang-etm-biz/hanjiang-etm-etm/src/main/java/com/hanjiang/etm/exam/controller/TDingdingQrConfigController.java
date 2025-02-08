package com.hanjiang.etm.exam.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TDingdingQrConfig;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.exam.service.TDingdingQrConfigService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/exam/qr")
@MenuTag(code = "etm_exam")
public class TDingdingQrConfigController {
    private final TDingdingQrConfigService tDingdingQrConfigService;

    public TDingdingQrConfigController(TDingdingQrConfigService tDingdingQrConfigService) {
        this.tDingdingQrConfigService = tDingdingQrConfigService;
    }

    @PreAuthorize("@ps.hasPermission('get::exam:qr:list')")
    @GetMapping(value = "/list",name = "二维码:列表")
    public R<?> page(){
        return R.data(tDingdingQrConfigService.list());
    }

    @PreAuthorize("@ps.hasPermission('post::exam:qr:add')")
    @PostMapping(value = "/add", name = "二维码:新增")
    public R<?> add(@RequestBody TDingdingQrConfig config){
        return R.data(tDingdingQrConfigService.save(config));
    }

    @PreAuthorize("@ps.hasPermission('get::exam:qr:select::id')")
    @GetMapping(value = "/select/{id}", name = "二维码:根据ID唯一获取")
    public R<?> selectById(@PathVariable Long id){
        return R.data(tDingdingQrConfigService.getById(id));
    }

    @PreAuthorize("@ps.hasPermission('post::exam:qr:edit')")
    @PostMapping(value = "/edit", name = "二维码:编辑")
    public R<?> edit(@RequestBody TDingdingQrConfig config){
        return R.data(tDingdingQrConfigService.updateById(config));
    }

    @PreAuthorize("@ps.hasPermission('post::exam:qr:delete')")
    @PostMapping(value = "/delete", name = "二维码:删除")
    public R<?> delete(@RequestBody List<Long> ids){
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("ID 不可为空");
        }
        return R.data(tDingdingQrConfigService.removeBatchByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('get::exam:qr:enable::id')")
    @GetMapping(value = "enable/{id}", name = "二维码:根据ID设置有效")
    @Transactional
    public R<?> changeEnable(@PathVariable Long id){
        // 置为有效
        UpdateWrapper<TDingdingQrConfig> uw = new UpdateWrapper<>();
        uw.eq("id", id);
        uw.set("enabled", 1);
        tDingdingQrConfigService.update(uw);

        // 置为无效
        UpdateWrapper<TDingdingQrConfig> uw2 = new UpdateWrapper<>();
        uw2.notIn("id", id);
        uw2.set("enabled", 0);
        tDingdingQrConfigService.update(uw2);

        return R.data(true);
    }

    @PreAuthorize("@ps.hasPermission('get::exam:qr:getEnabled')")
    @GetMapping(value = "getEnabled", name = "二维码:设置有效")
    public R<?> getEnabled(){
        QueryWrapper<TDingdingQrConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enabled", 1);
        return R.data(tDingdingQrConfigService.getOne(queryWrapper));
    }
}

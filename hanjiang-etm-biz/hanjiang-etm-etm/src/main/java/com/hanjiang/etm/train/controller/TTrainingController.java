package com.hanjiang.etm.train.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.dto.TTrainingDto;
import com.hanjiang.etm.common.entity.TTraining;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.mp.support.PageParams;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.common.vo.TTrainingVo;
import com.hanjiang.etm.train.export.TrainProgressExport;
import com.hanjiang.etm.train.service.TTrainingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 描述：培训
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/training")
@MenuTag(code = "etm_train")
public class TTrainingController {
    private final TTrainingService tTrainingService;
    private final TrainProgressExport trainProgressExport;

    public TTrainingController(TTrainingService tTrainingService, TrainProgressExport trainProgressExport) {
        this.tTrainingService = tTrainingService;
        this.trainProgressExport = trainProgressExport;
    }

    @PreAuthorize("@ps.hasPermission('get::training:page')")
    @GetMapping(value = "page", name = "分页")
    public R<?> page(TTraining training, PageParams pageParams) {
        if ("administrator".equals(AuthUtil.getUserCode())) {
            training.setCreateUser(null);
        } else {
            training.setCreateUser(String.valueOf(AuthUtil.getUserId()));
        }
        IPage<TTraining> pageList = tTrainingService.page(MP.getPage(pageParams), MP.getQueryWrapper(training));
        return R.data(pageList);
    }

    @PreAuthorize("@ps.hasPermission('post::training:add')")
    @PostMapping(value = "add", name = "新增")
    public R<?> add(@RequestBody TTrainingDto trainingDto) {
        return R.data(tTrainingService.saveV2(trainingDto));
    }

    @PreAuthorize("@ps.hasPermission('post::training:edit')")
    @PostMapping(value = "edit", name = "编辑")
    public R<?> edit(@RequestBody TTrainingDto trainingDto) {
        if (trainingDto.getId() == null) {
            throw new RuntimeException("ID 不可为空");
        }

        return R.data(tTrainingService.updateByIdV2(trainingDto));
    }

    @PreAuthorize("@ps.hasPermission('post::training:delete')")
    @PostMapping(value = "delete", name = "删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("ID 不可为空");
        }

        return R.data(tTrainingService.removeBatchByIdsV2(ids));
    }

    @PreAuthorize("@ps.hasPermission('get::training:select::id')")
    @GetMapping(value = "select/{id}", name = "根据ID唯一获取")
    public R<?> selectById(@PathVariable Long id) {
        return R.data(tTrainingService.getByIdV2(id));
    }

    @PreAuthorize("@ps.hasPermission('get::training:publish::id')")
    @GetMapping(value = "publish/{id}", name = "发布")
    public R<?> publish(@PathVariable Long id) {
        return R.data(tTrainingService.publish(id));
    }

    @PreAuthorize("@ps.hasPermission('get::training:cancelPublish::id')")
    @GetMapping(value = "cancelPublish/{id}", name = "撤销")
    public R<?> cancelPublish(@PathVariable Long id, int force) {
        return R.data(tTrainingService.cancelPublish(id, force));
    }

    // 获取用户培训信息
    @PreAuthorize("@ps.hasPermission('get::training:getUserTrainConfig')")
    @GetMapping(value = "getUserTrainConfig", name = "获取用户培训信息")
    public R<?> getUserTrainConfig(@RequestParam Long trainId, Long userId) {
        if (userId == null) {
            userId = AuthUtil.getUserId();
        } else if (!userId.equals(AuthUtil.getUserId()) && !"administrator".equals(AuthUtil.getUserCode())) {
            throw new RuntimeException("无权限进行此操作");
        }
        return R.data(tTrainingService.getUserTrainConfig(trainId, userId));
    }

    // 交卷
    @PreAuthorize("@ps.hasPermission('post::training:paperAnswer')")
    @PostMapping(value = "paperAnswer", name = "交卷")
    public R<?> paperAnswer(@RequestBody TTrainingVo body) {

        return R.data(tTrainingService.paperAnswer(body));
    }

    /**
     * 导出进展情况
     */
    @PreAuthorize("@ps.hasPermission('get::training:trainProgressExport')")
    @GetMapping(value = "/trainProgressExport", name = "进展情况:导出")
    public R<?> trainProgressExport(@RequestParam Long trainId, HttpServletResponse response) throws IOException {
        trainProgressExport.writeExcel(trainId, response);
        return R.data("ok");
    }
}

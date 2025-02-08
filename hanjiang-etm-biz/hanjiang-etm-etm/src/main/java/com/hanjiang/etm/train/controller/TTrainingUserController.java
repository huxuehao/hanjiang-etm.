package com.hanjiang.etm.train.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TTrainingUser;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.mp.support.PageParams;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.train.service.TTrainingUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * 描述：培训-用户
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/training/user")
@MenuTag(code = "etm_train")
public class TTrainingUserController {
    private final TTrainingUserService tTrainingUserService;

    public TTrainingUserController(TTrainingUserService tTrainingUserService) {
        this.tTrainingUserService = tTrainingUserService;
    }

    /**
     * 分页
     */
    @PreAuthorize("@ps.hasPermission('get::training:user:page')")
    @GetMapping(value = "page", name = "进展情况:分页")
    public R<?> page(TTrainingUser trainingUser, PageParams pageParams) {
        QueryWrapper<TTrainingUser> queryWrapper = MP.getQueryWrapper(trainingUser);
        queryWrapper.orderByDesc("paper_done","do_time","score");
        IPage<TTrainingUser> pageList = tTrainingUserService.page(MP.getPage(pageParams), queryWrapper);
        return R.data(pageList);
    }

    /**
     * 参与总人数
     * @param trainId 培训ID
     */
    @PreAuthorize("@ps.hasPermission('get::training:user:counts')")
    @GetMapping(value = "counts", name = "进展情况:参与人数")
    public R<?> page(@RequestParam Long trainId) {
        QueryWrapper<TTrainingUser> qw = new QueryWrapper<>();
        qw.eq("train_id", trainId);
        List<TTrainingUser> users = tTrainingUserService.list(qw);
        return R.data(users.size());
    }

    /**
     * 用户完成情况
     * @param trainId 培训ID
     */
    @PreAuthorize("@ps.hasPermission('get::training:user:completion-info')")
    @GetMapping(value = "completion-info", name = "进展情况:完成情况")
    public R<?> completion(@RequestParam Long trainId) {
        QueryWrapper<TTrainingUser> qw = new QueryWrapper<>();
        qw.eq("train_id", trainId);
        List<TTrainingUser> allUsers = tTrainingUserService.list(qw);
        qw.eq("paper_done", 1);
        List<TTrainingUser> doneUsers = tTrainingUserService.list(qw);
        return R.data(new HashMap<String, Integer>() {{
            put("all", allUsers.size());
            put("done", doneUsers.size());
        }});
    }

    /**
     * 用户合格情况
     * @param trainId 培训ID
     * @param score   合格分数
     */
    @PreAuthorize("@ps.hasPermission('get::training:user:qualified-info')")
    @GetMapping(value = "qualified-info", name = "进展情况:合格情况")
    public R<?> qualified(@RequestParam Long trainId, @RequestParam Integer score) {
        QueryWrapper<TTrainingUser> qw = new QueryWrapper<>();
        qw.eq("train_id", trainId);
        List<TTrainingUser> allUsers = tTrainingUserService.list(qw);
        qw.ge("score", score);
        List<TTrainingUser> doneUsers = tTrainingUserService.list(qw);
        return R.data(new HashMap<String, Integer>() {{
            put("all", allUsers.size());
            put("done", doneUsers.size());
        }});
    }
}

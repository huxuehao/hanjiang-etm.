package com.hanjiang.etm.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TExamUser;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.mp.support.PageParams;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.exam.service.TExamUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * 描述：考试用户进展
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/exam/user")
@MenuTag(code = "etm_exam")
public class TExamPaperUserController {

    private final TExamUserService tExamUserService;

    public TExamPaperUserController(TExamUserService tExamUserService){
        this.tExamUserService = tExamUserService;
    }

    /**
     * 分页
     */
    @PreAuthorize("@ps.hasPermission('get::exam:user:page')")
    @GetMapping(value = "/page", name = "进展情况:分页")
    public R<?> page(TExamUser tExamUser, PageParams pageParams){
        QueryWrapper<TExamUser> queryWrapper = MP.getQueryWrapper(tExamUser);
        queryWrapper.orderByDesc("paper_done","do_time","score");
        IPage<TExamUser> pageList = tExamUserService.page(MP.getPage(pageParams), queryWrapper);
        return R.data(pageList);
    }

    /**
     * 参与总人数
     */
    @PreAuthorize("@ps.hasPermission('get::exam:user:counts')")
    @GetMapping(value = "/counts", name = "进展情况:参数人数")
    public R<?> counts(@RequestParam Long examId){
        QueryWrapper<TExamUser> qw = new QueryWrapper<>();
        qw.eq("exam_id", examId);
        List<TExamUser> users = tExamUserService.list(qw);
        return R.data(users.size());
    }

    /**
     * 用户完成情况
     */
    @PreAuthorize("@ps.hasPermission('get::exam:user:completion')")
    @GetMapping(value = "completion", name = "进展情况:完成情况")
    public R<?> completion(@RequestParam Long examId){
        QueryWrapper<TExamUser> qw = new QueryWrapper<>();
        qw.eq("exam_id", examId);
        List<TExamUser> allUsers = tExamUserService.list(qw);
        qw.eq("paper_done", 1);
        List<TExamUser> doneUsers = tExamUserService.list(qw);
        return R.data(new HashMap<String, Integer>() {{
            put("all", allUsers.size());
            put("done", doneUsers.size());
        }});
    }

    /**
     * 用户合格情况
     */
    @PreAuthorize("@ps.hasPermission('get::exam:user:qualified')")
    @GetMapping(value = "qualified", name = "进展情况:合格情况")
    public R<?> qualified(@RequestParam Long examId,@RequestParam Integer score ){
        QueryWrapper<TExamUser> qw = new QueryWrapper<>();
        qw.eq("exam_id", examId);
        List<TExamUser> allUsers = tExamUserService.list(qw);
        qw.ge("score", score);
        List<TExamUser> doneUsers = tExamUserService.list(qw);
        return R.data(new HashMap<String, Integer>() {{
            put("all", allUsers.size());
            put("done", doneUsers.size());
        }});
    }
}

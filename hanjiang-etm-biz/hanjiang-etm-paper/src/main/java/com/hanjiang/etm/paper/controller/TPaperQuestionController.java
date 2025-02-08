package com.hanjiang.etm.paper.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TPaperQuestion;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.mp.support.PageParams;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.paper.service.TPaperQuestionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：试卷试题
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/paper/question")
@MenuTag(code = "etm_paper")
public class TPaperQuestionController {
    private final TPaperQuestionService paperQuestionService;

    public TPaperQuestionController(TPaperQuestionService paperQuestionService) {
        this.paperQuestionService = paperQuestionService;
    }

    @PreAuthorize("@ps.hasPermission('get::paper:question:page')")
    @GetMapping(value = "page", name = "试卷试题:分页")
    public R<?> page(TPaperQuestion paperQuestion, PageParams pageParams) {
        IPage<TPaperQuestion> pageList = paperQuestionService.page(MP.getPage(pageParams), MP.getQueryWrapper(paperQuestion));
        return R.data(pageList);
    }

    @PreAuthorize("@ps.hasPermission('post::paper:question:add')")
    @PostMapping(value = "add", name = "试卷试题:新增")
    public R<?> add(@RequestBody TPaperQuestion paperQuestion) {
        return R.data(paperQuestionService.save(paperQuestion));
    }

    @PreAuthorize("@ps.hasPermission('post::paper:question:edit')")
    @PostMapping(value = "edit", name = "试卷试题:编辑")
    public R<?> edit(@RequestBody TPaperQuestion paperQuestion) {
        if (paperQuestion.getId() == null) {
            throw new RuntimeException("ID 不可为空");
        }

        return R.data(paperQuestionService.updateById(paperQuestion));
    }

    @PreAuthorize("@ps.hasPermission('post::paper:question:delete')")
    @PostMapping(value = "delete", name = "试卷试题:删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("ID 不可为空");
        }

        return R.data(paperQuestionService.removeBatchByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('get::paper:question:select::id')")
    @GetMapping(value = "select/{id}", name = "试卷试题:根据ID唯一获取")
    public R<?> selectById(@PathVariable Long id) {
        return R.data(paperQuestionService.getById(id));
    }
}

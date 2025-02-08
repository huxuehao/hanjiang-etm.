package com.hanjiang.etm.exam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.dto.TDDInfoDto;
import com.hanjiang.etm.common.dto.TExamDto;
import com.hanjiang.etm.common.entity.TExam;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.mp.support.PageParams;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.exam.export.ExamProgressExport;
import com.hanjiang.etm.exam.service.TExamService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 描述：
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/exam/template")
@MenuTag(code = "etm_exam")
public class TExamController {
    private final TExamService tExamService;
    private final ExamProgressExport examProgressExport;

    public TExamController(TExamService tExamService, ExamProgressExport examProgressExport){
        this.tExamService = tExamService;
        this.examProgressExport = examProgressExport;
    }

    @PreAuthorize("@ps.hasPermission('get::exam:template:page')")
    @GetMapping(value = "/page", name = "分页")
    public R<?> page(TExam tExam, PageParams pageParams){
        if ("administrator".equals(AuthUtil.getUserCode())) {
            tExam.setCreateUser(null);
        } else {
            tExam.setCreateUser(String.valueOf(AuthUtil.getUserId()));
        }
        IPage<TExam> pageList = tExamService.page(MP.getPage(pageParams),MP.getQueryWrapper(tExam));
        return R.data(pageList);
    }

    @PreAuthorize("@ps.hasPermission('post::exam:template:add')")
    @PostMapping(value = "/add", name = "新增")
    public R<?> add(@RequestBody TExamDto tExamDto){
        return R.data(tExamService.addV2(tExamDto));
    }

    @PreAuthorize("@ps.hasPermission('get::exam:template:select::id')")
    @GetMapping(value = "select/{id}", name = "根据ID唯一获取")
    public R<?> selectById(@PathVariable Long id){
        return R.data(tExamService.selectByIdV2(id));
    }

    @PreAuthorize("@ps.hasPermission('post::exam:template:edit')")
    @PostMapping(value = "/edit", name = "编辑")
    public R<?> edit(@RequestBody TExamDto tExamDto){
        return R.data(tExamService.updateV2(tExamDto));
    }

    @PreAuthorize("@ps.hasPermission('post::exam:template:delete')")
    @PostMapping(value = "/delete", name = "删除")
    public R<?> delete(@RequestBody List<Long> ids){
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("ID 不可为空");
        }
        return R.data(tExamService.removeBatchByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('get::exam:template:publish::id')")
    @GetMapping(value = "/publish/{id}", name = "系统内部考试发布")
    public R<?> publish(@PathVariable Long id){
        return R.data(tExamService.publish(id));
    }

    @PreAuthorize("@ps.hasPermission('get::exam:template:publishV2::id')")
    @GetMapping(value = "/publishV2/{id}", name = "扫码开放考试发布")
    public R<?> publishV2(@PathVariable Long id){
        return R.data(tExamService.publishV2(id));
    }

    @PreAuthorize("@ps.hasPermission('get::exam:template:cancelPublish::id')")
    @GetMapping(value = "/cancelPublish/{id}", name = "系统内部考试撤销")
    public R<?> cancelPublish(@PathVariable Long id, @RequestParam Integer force){
        return R.data(tExamService.cancelPublish(id,1,force));
    }

    @PreAuthorize("@ps.hasPermission('get::exam:template:cancelPublishV2::id')")
    @GetMapping(value = "/cancelPublishV2/{id}", name = "扫码开放考试撤销")
    public R<?> cancelPublishV2(@PathVariable Long id, @RequestParam Integer force){
        return R.data(tExamService.cancelPublish(id,0, force));
    }

    @PreAuthorize("@ps.hasPermission('get::exam:template:examConfig')")
    @GetMapping(value = "/examConfig", name = "获取考试配置")
    public R<?> examConfig(@RequestParam Long examId,Long userId){
        if (userId == null) {
            userId = AuthUtil.getUserId();
        } else if (!userId.equals(AuthUtil.getUserId()) && !"administrator".equals(AuthUtil.getUserCode())) {
            throw new RuntimeException("无权限进行此操作");
        }
        return R.data(tExamService.selectExamConfig(examId,userId));
    }

    @PreAuthorize("@ps.hasPermission('get::exam:template:examConfigByCorrect')")
    @GetMapping(value = "/examConfigByCorrect", name = "批改模式获取试卷配置")
    public R<?> examConfigByCorrect(@RequestParam Long examId,@RequestParam Long userId){
        return R.data(tExamService.examConfigByCorrect(examId,userId));
    }

    /**
     * 交卷 + 机器批改（批改所有题型）
     */
    @PreAuthorize("@ps.hasPermission('post::exam:template:examAnswer')")
    @PostMapping(value = "/examAnswer", name = "交卷+机器批改")
    public R<?> examAnswer(@RequestBody TExamDto tExamTemplate){
        return R.data(tExamService.examAnswer(tExamTemplate));
    }

    /**
     * 交卷 + 人工批改（默认批改选择、判断）
     */
    @PreAuthorize("@ps.hasPermission('post::exam:template:examAnswerV2')")
    @PostMapping(value = "/examAnswerV2", name = "交卷+人工批改")
    public R<?> examAnswerV2(@RequestBody TExamDto tExamTemplate){
        return R.data(tExamService.examAnswerV2(tExamTemplate));
    }

    /**
     * 人工审批
     */
    @PreAuthorize("@ps.hasPermission('post::exam:template:correct')")
    @PostMapping(value = "/correct", name = "人工审批")
    public R<?> correct(@RequestBody TExamDto tExamTemplate){
        return R.data(tExamService.correct(tExamTemplate));
    }


    /**
     * 初始化考试，钉钉开放考试
     */
    @PreAuthorize("@ps.hasPermission('post::exam:template:initExamOfDD')")
    @PostMapping(value = "/initExamOfDD", name = "初始化考试(钉钉)")
    public R<?> initExamOfDD(@RequestBody TDDInfoDto tddInfoDto){
        return R.data(tExamService.initExamOfDD(tddInfoDto));
    }

    /**
     * 导出进展情况
     */
    @PreAuthorize("@ps.hasPermission('get::exam:template:examProgressExport')")
    @GetMapping(value = "/examProgressExport", name = "进展情况:导出")
    public R<?> examProgressExport(@RequestParam Long examId, HttpServletResponse response) throws IOException {
        examProgressExport.writeExcel(examId, response);
        return R.data("ok");
    }

    /**
     * 检测随机option是否合法
     */
    @PreAuthorize("@ps.hasPermission('post::exam:template:checkRandomOption')")
    @PostMapping(value = "/checkRandomOption", name = "检测随机option是否合法")
    public R<?> checkRandomOption(@RequestBody TExamDto examDto) {
        tExamService.checkRandomOption(examDto.getOption(), examDto.getClassifies());
        return R.data("ok");
    }
}

package com.hanjiang.etm.exam.export;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hanjiang.etm.common.dto.ProgressExportTemplate;
import com.hanjiang.etm.common.entity.TExam;
import com.hanjiang.etm.common.entity.TExamPaper;
import com.hanjiang.etm.common.entity.TExamUser;
import com.hanjiang.etm.exam.service.TExamPaperService;
import com.hanjiang.etm.exam.service.TExamService;
import com.hanjiang.etm.exam.service.TExamUserService;
import com.hanjiang.etm.common.dto.QuestionConfig;
import com.hanjiang.etm.question.factory.QuestionConfigFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：考试进展导出
 *
 * @author huxuehao
 **/
@Service
public class ExamProgressExport {
    private final TExamUserService tExamUserService;
    private final TExamPaperService tExamPaperService;
    private final TExamService tExamService;

    public ExamProgressExport(TExamUserService tExamUserService, TExamPaperService tExamPaperService, TExamService tExamService) {
        this.tExamUserService = tExamUserService;
        this.tExamPaperService = tExamPaperService;
        this.tExamService = tExamService;
    }

    public void writeExcel(Long examId, HttpServletResponse response) throws IOException {
        // 查询考试信息
        TExam exam = tExamService.getById(examId);

        // 计算总分
        QueryWrapper<TExamPaper> examPaperQW = new QueryWrapper<>();
        examPaperQW.eq("exam_id", examId);
        List<TExamPaper> examPapers = tExamPaperService.list(examPaperQW);
        int totalScore = 0;
        for (TExamPaper examPaper : examPapers) {
            QuestionConfig questionConfig = QuestionConfigFactory.buildInstance(examPaper.getConfig());
            totalScore += questionConfig.getScore();
        }

        ArrayList<ProgressExportTemplate> progressExportTemplates = new ArrayList<>();

        QueryWrapper<TExamUser> qw = new QueryWrapper<>();
        qw.eq("exam_id", examId);
        List<TExamUser> list = tExamUserService.list(qw);
        if (list != null && !list.isEmpty()) {
            for (TExamUser user : list) {
                ProgressExportTemplate exportTemplate = new ProgressExportTemplate();
                exportTemplate.setUserId(String.valueOf(user.getUserId()));
                exportTemplate.setUserCode(user.getUserCode());
                exportTemplate.setUserName(user.getUserName());
                exportTemplate.setOrgName(user.getOrgName());
                exportTemplate.setCompleted(user.getPaperDone() == 1 ? "已完成" : "未完成");
                if (user.getDoTime() != null) {
                    exportTemplate.setDoTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(user.getDoTime()));
                } else {
                    exportTemplate.setDoTime("--");
                }
                exportTemplate.setTotalScore(totalScore);
                exportTemplate.setQualifiedScore(exam.getSuccessScore());
                exportTemplate.setScore(user.getScore());
                exportTemplate.setQualified(user.getScore() >= exam.getSuccessScore() ? "合格" : "不合格");
                exportTemplate.setPlat(user.getPlat() != null && !"".equals(user.getPlat()) ? user.getPlat() : "--");
                progressExportTemplates.add(exportTemplate);
            }
        }

        EasyExcel.write(response.getOutputStream(), ProgressExportTemplate.class)
                .sheet(exam.getName())
                .doWrite(progressExportTemplates);
    }
}

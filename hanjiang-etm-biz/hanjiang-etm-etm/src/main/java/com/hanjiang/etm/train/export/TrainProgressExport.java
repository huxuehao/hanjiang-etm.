package com.hanjiang.etm.train.export;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hanjiang.etm.common.dto.ProgressExportTemplate;
import com.hanjiang.etm.common.entity.TTraining;
import com.hanjiang.etm.common.entity.TTrainingQuestion;
import com.hanjiang.etm.common.entity.TTrainingUser;
import com.hanjiang.etm.common.dto.QuestionConfig;
import com.hanjiang.etm.question.factory.QuestionConfigFactory;
import com.hanjiang.etm.train.service.TTrainingQuestionService;
import com.hanjiang.etm.train.service.TTrainingService;
import com.hanjiang.etm.train.service.TTrainingUserService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：培训进展导出
 *
 * @author huxuehao
 **/
@Service
public class TrainProgressExport {
    private final TTrainingUserService tTrainingUserService;
    private final TTrainingQuestionService tTrainingQuestionService;
    private final TTrainingService tTrainingService;

    public TrainProgressExport(TTrainingUserService tTrainingUserService, TTrainingQuestionService tTrainingQuestionService, TTrainingService tTrainingService) {
        this.tTrainingUserService = tTrainingUserService;
        this.tTrainingQuestionService = tTrainingQuestionService;
        this.tTrainingService = tTrainingService;
    }


    public void writeExcel(Long trainId, HttpServletResponse response) throws IOException {
        // 查询考试信息
        TTraining training = tTrainingService.getById(trainId);

        // 计算总分
        QueryWrapper<TTrainingQuestion> trainQuestionPaperQW = new QueryWrapper<>();
        trainQuestionPaperQW.eq("train_id", trainId);
        List<TTrainingQuestion> trainingQuestions = tTrainingQuestionService.list(trainQuestionPaperQW);
        int totalScore = 0;
        for (TTrainingQuestion trainingQuestion : trainingQuestions) {
            QuestionConfig questionConfig = QuestionConfigFactory.buildInstance(trainingQuestion.getConfig());
            totalScore += questionConfig.getScore();
        }

        ArrayList<ProgressExportTemplate> ProgressExportTemplates = new ArrayList<>();

        QueryWrapper<TTrainingUser> qw = new QueryWrapper<>();
        qw.eq("train_id", trainId);
        List<TTrainingUser> list = tTrainingUserService.list(qw);
        if (list != null && !list.isEmpty()) {
            for (TTrainingUser user : list) {
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
                exportTemplate.setQualifiedScore(training.getQualifiedScore());
                exportTemplate.setScore(user.getScore());
                exportTemplate.setQualified(user.getScore() >= training.getQualifiedScore() ? "合格" : "不合格");
                exportTemplate.setPlat(user.getPlat() != null && !"".equals(user.getPlat()) ? user.getPlat() : "--");
                ProgressExportTemplates.add(exportTemplate);
            }
        }

        EasyExcel.write(response.getOutputStream(), ProgressExportTemplate.class)
                .sheet(training.getTitle())
                .doWrite(ProgressExportTemplates);
    }


}

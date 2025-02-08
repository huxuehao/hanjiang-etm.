package com.hanjiang.etm.question.excelImport;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hanjiang.etm.common.dto.ImportQuestion;
import com.hanjiang.etm.common.dto.ImportQuestionParserBox;
import com.hanjiang.etm.common.entity.TQuestionTemplate;
import com.hanjiang.etm.question.excelImport.parser.ext.*;
import com.hanjiang.etm.common.dto.QuestionConfig;
import com.hanjiang.etm.question.factory.enums.QuestionType;
import com.hanjiang.etm.question.service.TQuestionTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 描述：
 *
 * @author huxuehao
 **/
@Service
public class ImportParserService {
    private final SingleQuestionParser singleQuestionParser;
    private final MultipleQuestionParser multipleQuestionParser;
    private final JudgeQuestionParser judgeQuestionParser;
    private final CompletionQuestionParser completionQuestionParser;
    private final QAQuestionParser qaQuestionParser;
    private final TQuestionTemplateService tQuestionTemplateService;

    public ImportParserService(SingleQuestionParser singleQuestionParser, MultipleQuestionParser multipleQuestionParser, JudgeQuestionParser judgeQuestionParser, CompletionQuestionParser completionQuestionParser, QAQuestionParser qaQuestionParser, TQuestionTemplateService tQuestionTemplateService) {
        this.singleQuestionParser = singleQuestionParser;
        this.multipleQuestionParser = multipleQuestionParser;
        this.judgeQuestionParser = judgeQuestionParser;
        this.completionQuestionParser = completionQuestionParser;
        this.qaQuestionParser = qaQuestionParser;
        this.tQuestionTemplateService = tQuestionTemplateService;
    }

    public ImportQuestionParserBox parserExcelToQuestion(MultipartFile file) throws IOException {
        ImportQuestionParserBox parserBox = new ImportQuestionParserBox();

        // 解析解答题
        Optional<ImportQuestion> singleQuestion = singleQuestionParser.doParser(file);
        singleQuestion.ifPresent((questions) -> {
            // 去除title重复的question
            List<? extends QuestionConfig> questionConfigs = filterEffectiveQuestion(questions, QuestionType.SINGLE);
            questions.setConfigs(questionConfigs);
            parserBox.setSingle(questions);
        });

        // 解析多选题
        Optional<ImportQuestion> multipleQuestion = multipleQuestionParser.doParser(file);
        multipleQuestion.ifPresent(questions -> {
            // 去除title重复的question
            List<? extends QuestionConfig> questionConfigs = filterEffectiveQuestion(questions, QuestionType.MULTIPLE);
            questions.setConfigs(questionConfigs);
            parserBox.setMultiple(questions);
        });

        // 解析判断题
        Optional<ImportQuestion> judgeQuestion = judgeQuestionParser.doParser(file);
        judgeQuestion.ifPresent(questions -> {
            // 去除title重复的question
            List<? extends QuestionConfig> questionConfigs = filterEffectiveQuestion(questions, QuestionType.JUDGE);
            questions.setConfigs(questionConfigs);
            parserBox.setJudge(questions);
        });

        // 解析填空题
        Optional<ImportQuestion> completionQuestion = completionQuestionParser.doParser(file);
        completionQuestion.ifPresent(questions -> {
            // 去除title重复的question
            List<? extends QuestionConfig> questionConfigs = filterEffectiveQuestion(questions, QuestionType.COMPLETION);
            questions.setConfigs(questionConfigs);
            parserBox.setCompletion(questions);
        });

        // 解析解答题
        Optional<ImportQuestion> qaQuestion = qaQuestionParser.doParser(file);
        qaQuestion.ifPresent(questions -> {
            // 去除title重复的question
            List<? extends QuestionConfig> questionConfigs = filterEffectiveQuestion(questions, QuestionType.QA);
            questions.setConfigs(questionConfigs);
            parserBox.setQa(questions);
        });

        return parserBox;
    }

    /**
     * 过滤有效的试题，如果数据库中可以匹配到相同的题目，因为已存在，那么无效
     * @param questions 待过滤的实体
     * @param type      试题类型
     */
    public List<? extends QuestionConfig>  filterEffectiveQuestion(ImportQuestion questions, QuestionType type) {
        // 去除title重复的question
        List<String> titles = questions.getConfigs().stream().map(QuestionConfig::getTitle).collect(Collectors.toList());
        QueryWrapper<TQuestionTemplate> qw = new QueryWrapper<>();
        qw.in("title", titles);
        qw.eq("type", type.value);
        List<TQuestionTemplate> alreadyExistList = tQuestionTemplateService.list(qw);
        // 过滤不存在的question
        return  questions.getConfigs().stream().filter(item -> {
            for (TQuestionTemplate tQuestionTemplate : alreadyExistList) {
                if (item.getTitle().equals(tQuestionTemplate.getTitle())) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
    }
}

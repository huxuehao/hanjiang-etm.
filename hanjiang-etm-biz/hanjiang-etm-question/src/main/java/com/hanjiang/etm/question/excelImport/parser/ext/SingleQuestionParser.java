package com.hanjiang.etm.question.excelImport.parser.ext;

import com.hanjiang.etm.common.dto.ImportQuestion;
import com.hanjiang.etm.common.entity.TQuestionClassify;
import com.hanjiang.etm.question.excelImport.parser.QuestionParser;
import com.hanjiang.etm.question.factory.Option;
import com.hanjiang.etm.question.factory.enums.QuestionType;
import com.hanjiang.etm.question.factory.template.SingleQuestionConfig;
import com.hanjiang.etm.question.service.TQuestionClassifyService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * 描述：单选题excel解析器
 *
 * @author huxuehao
 **/
@Service
public class SingleQuestionParser extends QuestionParser {
    public static List<String> defaultHeads = Arrays.asList("分类", "名称", "题干", "解析","分数","难度","共享","答案");
    private final TQuestionClassifyService tQuestionClassifyService;

    public SingleQuestionParser(TQuestionClassifyService tQuestionClassifyService) {
        this.tQuestionClassifyService = tQuestionClassifyService;
    }

    @Override
    public Optional<ImportQuestion> doParser(MultipartFile file) throws IOException {
        // 存储试题配置
        List<SingleQuestionConfig> configs = new LinkedList<>();
        // 存储新增的分类ID
        List<Long> classifyIds = new ArrayList<>();
        boolean existError = true;
        try (Workbook workbook = getWorkbook(file)) {
            //获取目标sheet
            Sheet sheet = getTargetSheet(workbook, "单选题");
            if (sheet == null) {
                return Optional.empty();
            }

            // 判断导入最大数
            if (sheet.getLastRowNum() > 502) {
                throw new RuntimeException("单选题一次性导入最多500条");
            }

            // 新增未知类型
            List<TQuestionClassify> classifyList = tQuestionClassifyService.list();

            // 遍历所有有数据行的试题配置
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 2; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                // 处理断层
                if (row == null) {
                    continue;
                }

                if ((sheet.getRow(1).getLastCellNum() - defaultHeads.size()) > 9) {
                    throw new RuntimeException("最多支持9个选项");
                }

                SingleQuestionConfig questionConfig = new SingleQuestionConfig();
                questionConfig.setType(QuestionType.SINGLE.value);
                questionConfig.setOption(new ArrayList<>());
                questionConfig.setSuccess(null);
                for (int j = 0; j < sheet.getRow(1).getLastCellNum(); j++) {
                    // 条件成立：默认列
                    if (j <= defaultHeads.size() - 1) {
                        switch (j) {
                            case 0:
                                String classifyName = getStringCellValue(row, j, true).trim();
                                for (TQuestionClassify tQuestionClassify : classifyList) {
                                    if (classifyName.equals(tQuestionClassify.getName())) {
                                        questionConfig.setClassifyId(tQuestionClassify.getId());
                                    }
                                }
                                if (questionConfig.getClassifyId() == null) {
                                    TQuestionClassify classify = new TQuestionClassify();
                                    classify.setName(classifyName);
                                    classify.setCode("fromExcel");
                                    classify.setParentId(0L);
                                    classify.setDelFlag(0);
                                    classify.setExcel(1);
                                    tQuestionClassifyService.save(classify);
                                    classifyIds.add(classify.getId());
                                    questionConfig.setClassifyId(classify.getId());
                                    classifyList.add(classify);
                                }
                                break;
                            case 1:
                                questionConfig.setName(getStringCellValue(row, j, true).trim());
                                break;
                            case 2:
                                questionConfig.setTitle(getStringCellValue(row, j, true).trim());
                                break;
                            case 3:
                                questionConfig.setAnalysis(getStringCellValue(row, j, false));
                                break;
                            case 4:
                                questionConfig.setScore(getIntCellValue(row, j, true));
                                break;
                            case 5:
                                questionConfig.setLevel(getIntCellValue(row, j, true));
                                break;
                            case 6:
                                String value = getStringCellValue(row, j, true).trim();
                                if ("否".equals(value)) {
                                    questionConfig.setShare(0);
                                } else {
                                    questionConfig.setShare(1);
                                }
                                break;
                            case 7:
                                questionConfig.setAnswer(getStringCellValue(row, j, true).trim());
                                break;
                        }
                    }
                    // 条件成立：选项列
                    else {
                        int index = j - (defaultHeads.size() - 1);
                        Option option = new Option(numberToLetter(index), getStringCellValue(row, j, true), 0, index);
                        questionConfig.getOption().add(option);
                    }
                }
                configs.add(questionConfig);
            }

            /**
             * 处理答案，将文字转成A、B、C...
             */
            // 获取选项标题与letter的对照
            Row row = sheet.getRow(1); // 获取title行
            int answerNum = row.getLastCellNum() - defaultHeads.size(); // answerNum为excel中用户设置的选项数量
            HashMap<String, String> answerMap = new HashMap<>();
            for (int i = 0; i < answerNum; i++) {
                String answerMapKey = getStringCellValue(row, i + defaultHeads.size(), true).trim();
                String answerMapValue = numberToLetter(i + 1);
                answerMap.put(answerMapKey, answerMapValue);
            }
            // 将config中answer的文本换成ABCD..
            for (SingleQuestionConfig config : configs) {
                String letter = answerMap.get(config.getAnswer());
                if (letter != null) {
                    config.setAnswer(letter);
                } else {
                    throw new RuntimeException("存在答案列与选项标题不匹配");
                }
            }
            existError = false;
        } finally {
            if (existError && !classifyIds.isEmpty()) {
                tQuestionClassifyService.removeBatchByIds(classifyIds);
            }
        }

        return Optional.of(new ImportQuestion().setClassifyIds(classifyIds).setConfigs(configs));
    }
}

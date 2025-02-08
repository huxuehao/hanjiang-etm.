package com.hanjiang.etm.exam.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.dto.TDDInfoDto;
import com.hanjiang.etm.common.dto.TExamDto;
import com.hanjiang.etm.common.entity.*;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.exam.mapper.TExamMapper;
import com.hanjiang.etm.common.dto.QuestionConfig;
import com.hanjiang.etm.question.factory.QuestionConfigFactory;
import com.hanjiang.etm.question.service.TQuestionTemplateService;
import com.hanjiang.etm.task.service.TTaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述：
 *
 * @author huxuehao
 **/
@Service
public class TExamServiceImpl extends ServiceImpl<TExamMapper, TExam> implements TExamService {
    private final TExamPaperService tExamPaperService;
    private final TExamPaperUserService tExamPaperUserService;
    private final TExamCorrectorService tExamCorrectorService;
    private final TExamUserService tExamUserService;
    private final TExamMapper tExamMapper;
    private final TTaskService tTaskService;
    private final TExamClassifyService tExamClassifyService;
    private final TExamRandomOptionService tExamRandomOptionService;
    private final TQuestionTemplateService tQuestionTemplateService;
    private final Map<Integer, String> questionTable = new HashMap<Integer, String>() {{
       put(1, "单选题");
       put(2, "多选题");
       put(3, "判断题");
       put(4, "填空题");
       put(5, "解答题");
    }};

    public TExamServiceImpl(TExamPaperService tExamPaperService, TExamPaperUserService tExamPaperUserService, TExamCorrectorService tExamCorrectorService, TExamUserService tExamUserService, TExamMapper tExamMapper, TTaskService tTaskService, TExamClassifyService tExamClassifyService, TExamRandomOptionService tExamRandomOptionService, TQuestionTemplateService tQuestionTemplateService) {
        this.tExamPaperService = tExamPaperService;
        this.tExamPaperUserService = tExamPaperUserService;
        this.tExamCorrectorService = tExamCorrectorService;
        this.tExamUserService = tExamUserService;
        this.tExamMapper = tExamMapper;
        this.tTaskService = tTaskService;
        this.tExamClassifyService = tExamClassifyService;
        this.tExamRandomOptionService = tExamRandomOptionService;
        this.tQuestionTemplateService = tQuestionTemplateService;
    }

    /**
     * 检测考试配置
     * @param tExamDto 考试DTO
     */
    private void checkExamConfig(TExamDto tExamDto) {
        if (tExamDto.getCheckType() == 1 && (tExamDto.getCorrector() == null || tExamDto.getCorrector().isEmpty())) {
            throw new RuntimeException("当前考试为人工审批，请配置审批人员");
        }
        if (tExamDto.getCurrentType() == 1 && (tExamDto.getUser() == null || tExamDto.getUser().isEmpty())) {
            throw new RuntimeException("请配置考试人员");
        }
        if (tExamDto.getQuestionRule() > 0 && (tExamDto.getOption() == null || tExamDto.getOption().isEmpty())) {
            throw new RuntimeException("请配置试卷题目");
        }
        if(tExamDto.getQuestionRule() == 0 && (tExamDto.getQuestion() == null || tExamDto.getQuestion().isEmpty())) {
            throw new RuntimeException("请配置试卷题目");
        }
    }

    /**
     * 保存字表
     * @param tExamDto 考试DTO
     * @param examId  考试ID
     */
    private void safeSaveSubTable(TExamDto tExamDto, Long examId) {
        // 保存考试用户
        if (tExamDto.getCurrentType() == 1) {
            tExamUserService.safeSaveBatch(examId, tExamDto.getUser());
        }
        // 保存考试审批
        if (tExamDto.getCheckType() == 1) {
            tExamCorrectorService.safeSaveBatch(examId, tExamDto.getCorrector());
        }
        // 条件成立：当前模式为固定试题
        if (tExamDto.getQuestionRule() == 0) {
            tExamPaperService.safeSaveBatch(examId, tExamDto.getQuestion());
        }
        // 条件成立：当前模式为随机试题
        else {
            tExamRandomOptionService.safeSaveBatch(examId, tExamDto.getOption());
            // 条件成立：题目随机，试卷相同
            if (tExamDto.getQuestionRule() == 2) {
                TExam tExam = new TExam();
                BeanUtils.copyProperties(tExamDto, tExam);
                tExam.setId(examId);
                // 基于随机试题配置生成模版
                genPaperTemplateOfRandomFixed(tExam);
            }
        }
    }

    @Override
    @Transactional
    public boolean addV2(TExamDto tExamDto) {
        // 检测配置
        checkExamConfig(tExamDto);
        // 构建并保存主表
        TExam tExam = new TExam();
        BeanUtils.copyProperties(tExamDto, tExam);
        tExam.setCreateUser(String.valueOf(AuthUtil.getUserId()));
        tExam.setCreateTime(new Date());
        tExam.setPublished(0);
        save(tExam);
        // 保存字表
        safeSaveSubTable(tExamDto, tExam.getId());
        return true;
    }

    @Override
    @Transactional
    public boolean updateV2(TExamDto tExamDto) {
        // 检测配置
        checkExamConfig(tExamDto);
        // 跟新主表
        TExam tExam = new TExam();
        BeanUtils.copyProperties(tExamDto, tExam);
        tExamMapper.updateById(tExam);
        // 保存子表
        safeSaveSubTable(tExamDto, tExam.getId());
        return true;
    }

    @Override
    public TExamDto selectByIdV2(Long id) {
        // 查询 主表信息
        TExam tExam = this.baseMapper.selectById(id);
        TExamDto tExamDto = new TExamDto();
        BeanUtils.copyProperties(tExam, tExamDto);
        // 查询用户信息
        tExamDto.setUser(tExamUserService.selectListByExamId(id));
        // 条件成立：题目固定，试卷相同
        // 条件成立：题目随机，试卷相同
        if (tExam.getQuestionRule() != 1) {
            // 查询试卷信息
            tExamDto.setQuestion(tExamPaperService.selectObjectListByExamId(id));
        }
        // 条件成立：题目随机，试卷不同
        // 条件成立：题目随机，试卷相同
        if (tExam.getQuestionRule() != 0) {
            tExamDto.setOption(tExamRandomOptionService.selectListByExamId(id));
        }
        // 查询考试审批
        if (tExam.getCheckType() == 1) {
            tExamDto.setCorrector(tExamCorrectorService.selectListByExamId(id));
        }
        return tExamDto;
    }

    /**
     * 发布系统内部考试
     */
    @Override
    @Transactional
    public boolean publish(Long id) {
        // 更新主表
        TExam tExam = getById(id);
        // 发布前检测
        checkBeforePublish(tExam,1);
        // 获取用户
        List<TExamUser> tExamUsers = tExamUserService.selectListByExamId(id);
        // 条件成立：题目随机，试卷不同
        if (tExam.getQuestionRule() == 1) {
            genUserPaperOfRandom(tExam, tExamUsers);
        }
        // 条件成立：题目随机，试卷相同
        // 条件成立：题目固定，试卷相同
        else {
            genUserPaperOfFixed(tExam, tExamUsers);
        }
        // 更新主表
        Date publishTime = new Date();
        tExam.setPublished(1);
        tExam.setPublishTime(publishTime);
        updateById(tExam);

        // 完成考试的发布，接入t_user_show，每100条入一次库
        TExamClassify tExamClassify = tExamClassifyService.getById(tExam.getClassifyId());
        tTaskService.accessByPublished(
                tExamUsers.stream().map(TExamUser::getUserId).collect(Collectors.toList()),
                0,
                (task) -> {
                    task.setExamTranId(tExam.getId());
                    task.setClassifyId(tExam.getClassifyId());
                    task.setClassifyName(tExamClassify.getName());
                    task.setShowName(tExam.getName());
                    task.setPublishTime(tExam.getPublishTime());
                    task.setLimitEndTime(tExam.getLimitEndTime());
                    task.setLimitStartTime(tExam.getLimitStartTime());
                }
        );

        return true;
    }

    /**
     * 题目固定，试卷相同
     * @param tExam     考试实体
     * @param examUsers 参数考试的用户
     */
    private void genUserPaperOfFixed(TExam tExam, List<TExamUser> examUsers) {
        // 获取试题
        List<TExamPaper> tExamPapers = tExamPaperService.selectListByExamId(tExam.getId());

        // 生成用户试题，每100条入一次库
        int tag  = 0;
        List<TExamPaperUser> tExamPaperUsers = new ArrayList<>();
        for (TExamUser tExamUser : examUsers) {
            for (TExamPaper tExamPaper : tExamPapers) {
                TExamPaperUser tExamPaperUser = new TExamPaperUser();
                tExamPaperUser.setExamId(tExam.getId());
                tExamPaperUser.setExamPaperId(tExamPaper.getId());
                tExamPaperUser.setUserId(tExamUser.getUserId());
                tExamPaperUser.setConfig(tExamPaper.getConfig());
                tExamPaperUser.setPaperDone(0);
                tExamPaperUser.setDelFlag(0);
                tExamPaperUser.setSort(tExamPaper.getSort());
                tExamPaperUsers.add(tExamPaperUser);
                tag++;
                if (tag % 100 == 0) {
                    tExamPaperUserService.saveBatch(tExamPaperUsers);
                    tExamPaperUsers = new ArrayList<>();
                } else if (tag == (examUsers.size() * tExamPapers.size())) {
                    tExamPaperUserService.saveBatch(tExamPaperUsers);
                }
            }
        }
    }

    /**
     * 题目随机，试卷不同
     * @param tExam     考试实体
     * @param examUsers 参数考试的用户
     */
    private void genUserPaperOfRandom(TExam tExam, List<TExamUser> examUsers) {
        // 查询试题配置
        List<TExamRandomOption> examRandomOptions = tExamRandomOptionService.selectListByExamId(tExam.getId());

        // 检测配置的合理性，获取资题目源池
        Map<Integer, List<TQuestionTemplate>> questionResourcePool = checkRandomOptionAndGetPool(examRandomOptions, tExam.getClassifies());

        // 基于获取到的资源池为每个
        for (TExamUser examUser : examUsers) {
            // 遍历题目配置
            int sort = 1;
            for (TExamRandomOption option : examRandomOptions) {
                // 获取生成的随机题目
                List<TQuestionTemplate> randomQuestions = randomQuestionByOption(option, questionResourcePool.get(option.getQuestionType()));
                ArrayList<TExamPaperUser> tExamPaperUsers = new ArrayList<>();
                for (TQuestionTemplate randomQuestion : randomQuestions) {
                    // 重置分数
                    QuestionConfig questionConfig = QuestionConfigFactory.buildInstance(randomQuestion.getConfig());
                    questionConfig.setScore(option.getQuestionScore());
                    //构建ExamPaperUser对象
                    TExamPaperUser tExamPaperUser = new TExamPaperUser();
                    tExamPaperUser.setExamId(tExam.getId());
                    tExamPaperUser.setExamPaperId(null);
                    tExamPaperUser.setUserId(examUser.getUserId());
                    tExamPaperUser.setConfig(questionConfig.toJson());
                    tExamPaperUser.setPaperDone(0);
                    tExamPaperUser.setDelFlag(0);
                    tExamPaperUser.setSort(sort);
                    tExamPaperUsers.add(tExamPaperUser);
                    sort++;
                }
                // 批量入库：考试试卷用户表
                tExamPaperUserService.saveBatch(tExamPaperUsers);
            }
        }
    }
    /**
     * 题目随机，试卷相同，生成试卷模版
     * @param tExam  考试实体
     */
    private void genPaperTemplateOfRandomFixed(TExam tExam) {
        // 查询试题配置
        List<TExamRandomOption> examRandomOptions = tExamRandomOptionService.selectListByExamId(tExam.getId());

        // 检测配置的合理性，获取资题目源池
        Map<Integer, List<TQuestionTemplate>> questionResourcePool = checkRandomOptionAndGetPool(examRandomOptions, tExam.getClassifies());
        // 生成考试试卷
        int sort = 1;
        for (TExamRandomOption option : examRandomOptions) {
            // 获取生成的随机题目
            List<TExamPaper> tExamPapers = new ArrayList<>();
            List<TQuestionTemplate> randomQuestions = randomQuestionByOption(option, questionResourcePool.get(option.getQuestionType()));
            for (TQuestionTemplate randomQuestion : randomQuestions) {
                // 重置分数
                QuestionConfig questionConfig = QuestionConfigFactory.buildInstance(randomQuestion.getConfig());
                questionConfig.setScore(option.getQuestionScore());
                TExamPaper tExamPaper = new TExamPaper();
                tExamPaper.setExamId(tExam.getId());
                tExamPaper.setConfig(questionConfig.toJson());
                tExamPaper.setSort(sort);
                tExamPapers.add(tExamPaper);
                sort++;
            }
            // 批量入库：考试试卷用户表
            tExamPaperService.saveBatch(tExamPapers);
        }
    }

    /**
     * 检测随机option是否合法，并获取资源池
     * @param options    option配置
     * @param classifies 分类字符串
     * @return  题目资源池
     */
    private Map<Integer, List<TQuestionTemplate>>  checkRandomOptionAndGetPool(List<TExamRandomOption> options, String classifies) {
        Map<Integer, List<TQuestionTemplate>> questionResourcePool = new HashMap<>();
        for (TExamRandomOption option : options) {
            // 获取自定类型的题目资源
            List<TQuestionTemplate> list = tQuestionTemplateService.getQuestionPool(option.getQuestionType(), classifies);
            if (list.size() < option.getQuestionNum()) {
                throw new RuntimeException("现有的【"+questionTable.get(option.getQuestionType())+"】数量为"+list.size()+"，小于"+option.getQuestionNum());
            } else {
                questionResourcePool.put(option.getQuestionType(), list);
            }
        }

        return questionResourcePool;
    }

    /**
     * 检测随机option是否合法
     * @param options    option配置
     * @param classifies 分类字符串
     */
    public void checkRandomOption(List<TExamRandomOption> options, String classifies) {
        for (TExamRandomOption option : options) {
            // 获取自定类型的题目资源
            List<TQuestionTemplate> list = tQuestionTemplateService.getQuestionPool(option.getQuestionType(), classifies);
            if (list.size() < option.getQuestionNum()) {
                throw new RuntimeException("现有的【"+questionTable.get(option.getQuestionType())+"】数量为"+list.size()+"，小于"+option.getQuestionNum());
            }
        }
    }

    /**
     * 根基option生成随机的题目
     * @param option    option
     * @param questions 题目资源
     * @return  随机生成的题目
     */
    private List<TQuestionTemplate> randomQuestionByOption(TExamRandomOption option,  List<TQuestionTemplate> questions) {
        // 合法性检测
        if (option.getQuestionNum() > questions.size()) {
            throw new RuntimeException("题目资源不足");
        }
        // 随机打乱数组
        Collections.shuffle(questions);
        return questions.subList(0, option.getQuestionNum());
    }

    /**
     * 发布扫码开放考试
     */
    @Override
    public boolean publishV2(Long id) {
        // 更新主表
        TExam tExam = getById(id);

        // 发布前检测
        checkBeforePublish(tExam,0);

        // 检测随机option是否合法
        List<TExamRandomOption> options = tExamRandomOptionService.selectListByExamId(tExam.getId());
        checkRandomOption(options, tExam.getClassifies());

        // 更新主表
        Date publishTime = new Date();
        tExam.setPublished(1);
        tExam.setPublishTime(publishTime);
        updateById(tExam);

        return true;
    }

    /**
     * 发布之前检查
     * @param tExam 考试实体
     * @param type  类型（0外部考试，1内部考试）
     */
    private void checkBeforePublish(TExam tExam, int type) {
        if (tExam == null) {
            throw new RuntimeException("当前考试不存在");
        } else if (tExam.getPublished() == 1) {
            throw new RuntimeException("当前考试已发布，请勿重复操作");
        } else if (checkExamTimeout(tExam)) {
            throw new RuntimeException("当前考试已超时，请重新设置结束时间");
        } else if (tExam.getCurrentType() == 1 && type == 0) {
            throw new RuntimeException("当前考试发布不支持【系统内部考试】类型");
        } else if (tExam.getCurrentType() == 0 && type == 1) {
            throw new RuntimeException("当前考试发布不支持【扫码开放考试】类型");
        }
    }

    @Override
    @Transactional
    public Object cancelPublish(Long id, int type, Integer force) {
        if (force == 1) {
            initQuestionUserByExamId(id, type);
        } else {
            if (!tExamPaperUserService.haveDoneUserByExamId(id)) {
                initQuestionUserByExamId(id, type);
            } else {
                throw new RuntimeException("操作被拒绝，已有用户完成答题");
            }
        }
        // 更新主表
        TExam tExam = getById(id);
        tExam.setPublished(0);
        tExam.setPublishTime(null);
        updateById(tExam);

        // 解除考试发布，接入t_user_show
        tTaskService.deleteByMixinsId(tExam.getId());
        return true;
    }

    /**
     * 初始化用户痕迹
     * @param id    examId
     * @param type  类型（0外部考试，1内部考试）
     */
    void initQuestionUserByExamId(Long id, int type) {
        // 删除t_exam_paper_user
        tExamPaperUserService.deleteByExamId(id);
        // 重置t_exam_user
        if (type == 1) {
            tExamUserService.resetUserAnswer(id);
        } else {
            tExamUserService.deleteByExamId(id);
        }

    }

    @Override
    public TExamDto selectExamConfig(Long examId, Long userId) {
        // 查询主表
        TExam tExam = getById(examId);
        // 检测考试是否过期
        if (tExam == null) {
            throw new RuntimeException("当前考试不存在");
        } else if (tExam.getPublished() == 0) {
            throw new RuntimeException("当前考试暂未发布，无法继续考试");
        }

        // 查询用户字表
        List<TExamUser> tExamUsers = tExamUserService.getOneUserInfo(examId, userId);
        if (tExamUsers.size() != 1) {
            throw new RuntimeException("匹配失败，从考试中获取到多个用户信息");
        }

        if (checkExamTimeout(tExam)) {
            if (tExamUsers.get(0).getPaperDone() == 0) {
                throw new RuntimeException("您已错过考试有效期，无法继续考试");
            }
        } else if (checkExamNotStart(tExam)) {
            throw new RuntimeException("本场考试还未开始，无法继续考试");
        }

        TExamDto tExamDto = new TExamDto();
        BeanUtils.copyProperties(tExam, tExamDto);
        tExamDto.setUser(tExamUsers);

        // 查询试题表
        tExamDto.setQuestion(tExamPaperUserService.getOneUserPaper(examId, userId));

        // 判断用户是否完成答题,接入user_show
        if (tExamUsers.get(0).getPaperDone() == 0) {
            tTaskService.updateDoStartTime(examId, userId, 0);
        }

        return tExamDto;
    }

    // 检查考试是否过期
    public boolean checkExamTimeout(TExam tExam) {
        Date limitEndTime = tExam.getLimitEndTime();
        if (limitEndTime != null) {
            return new Date().getTime() > limitEndTime.getTime();
        }
        throw new RuntimeException("未找到截止时间，请联系管理员");
    }
    // 检查考试是否开始
    public boolean checkExamNotStart(TExam tExam) {
        Date limitStartTime = tExam.getLimitStartTime();
        if (limitStartTime != null ) {
            return new Date().getTime() < limitStartTime.getTime();
        }
        throw new RuntimeException("未找到截止时间，请联系管理员");
    }

    @Override
    @Transactional
    public Object initExamOfDD(TDDInfoDto tddInfoDto) {
        // 查询主表
        TExam tExam = getById(tddInfoDto.getExamId());
        // 检测考试是否过期
        if (tExam == null) {
            throw new RuntimeException("考试不存在");
        } else if (tExam.getPublished() == 0) {
            throw new RuntimeException("考试暂未发布");
        }

        // 查看用户是否已经存在
        List<TExamUser> oneUserInfo = tExamUserService.getOneUserInfo(tddInfoDto.getExamId(), Long.valueOf(tddInfoDto.getUserId()));
        if (oneUserInfo != null && oneUserInfo.size() == 1) {
            return oneUserInfo.get(0);
        } else if(checkExamTimeout(tExam))  {
            throw new RuntimeException("考试已过期");
        } else if (checkExamNotStart(tExam)) {
            throw new RuntimeException("考试未开始");
        }

        // 保存考试用户信息
        TExamUser tExamUser = tExamUserService.saveUserInfoOfApp(tddInfoDto);

        // 删除，避免重复
        tExamPaperUserService.deleteOneByIds(tExamUser.getExamId(), tExamUser.getUserId());

        // 条件成立：题目随机，试卷不同
        if (tExam.getQuestionRule() == 1) {
            genUserPaperOfRandom(tExam, Collections.singletonList(tExamUser));
        }
        // 条件成立：题目固定，试卷相同
        // 条件成立：题目随机，试卷相同
        else {
            genUserPaperOfFixed(tExam, Collections.singletonList(tExamUser));
        }

        // 人工审批
        if (tExam.getCheckType() == 1) {
            // 接入user_show
            TTask task = new TTask();
            task.setExamTranId(tExam.getId());
            task.setClassifyId(tExam.getClassifyId());
            task.setClassifyName(null);
            task.setShowName(tExam.getName());
            task.setUserCode(String.valueOf(tExamUser.getUserId()));
            task.setPublishTime(tExam.getPublishTime());
            task.setLimitEndTime(tExam.getLimitEndTime());
            task.setLimitStartTime(tExam.getLimitStartTime());
            task.setShowType(0);
            task.setShowStatus(0);
            task.setValid(1);
            tTaskService.save(task);
        }
        return tExamUser;
    }

    /**
     * 用户交卷 + 自动审批
     */
    @Override
    @Transactional
    public Object examAnswer(TExamDto body) {
        if (body.getUser().size() != 1) {
            throw new RuntimeException("用户信息不匹配，提交失败");
        }

        // 自动审题+自动计算成绩+更新t_Exam_question_user
        int score = 0; // 总得分
        List<Object> question = body.getQuestion();
        for (Object item : question) {
            String questionConfigStr = JSON.toJSONString(item);
            // 构建出试题对象
            QuestionConfig questionConfig = QuestionConfigFactory.buildInstance(questionConfigStr);
            // 审批题目
            questionConfig.doCorrect();
            // 获取分数
            score += questionConfig.getScoreBySuccess();

            // 更新t_exam_paper_user
            Long examPaperUserId = JSON.parseObject(questionConfigStr).getLong("examPaperUserTableId");
            tExamPaperUserService.updateUserPaperWithCorrected(examPaperUserId, questionConfig);
        }

        // 获取用户信息
        TExamUser tExamUser = body.getUser().get(0);
        // 更新用户信息
        Date date = tExamUserService.updateUserWithCorrected(tExamUser, score);

        // 用户完成考试，接入t_user_show
        tTaskService.updateByCorrected(date, score,(qw) -> {
            qw.eq("exam_tran_id",tExamUser.getExamId());
            qw.eq("user_code",tExamUser.getUserId());
            qw.eq("show_type",0);
        });

        return true;
    }

    /**
     * 用户交卷
     */
    @Override
    @Transactional
    public Object examAnswerV2(TExamDto body) {
        if (body.getUser().size() != 1) {
            throw new RuntimeException("用户信息不匹配，提交失败");
        }

        // 更新t_exam_question_user
        List<Object> question = body.getQuestion();
        for (Object item : question) {
            String questionConfigStr = JSON.toJSONString(item);
            // 构建出试题对象
            QuestionConfig questionConfig = QuestionConfigFactory.buildInstance(questionConfigStr);
            // 对于一些默认支持自动批改的题目（选择、判断）进行自动批改
            if (questionConfig.defaultAutoCorrect()) {
                questionConfig.doCorrect();
            }
            // 更新t_exam_paper_user
            Long examPaperUserTableId = JSON.parseObject(questionConfigStr).getLong("examPaperUserTableId");
            tExamPaperUserService.updateUserPaperWithCorrectedV2(examPaperUserTableId, questionConfig);
        }

        // 获取用户信息
        TExamUser tExamUser = body.getUser().get(0);
        // 更新用户信息
        Date date = tExamUserService.updateUserWithCorrected(tExamUser, null);

        // 用户完成考试，接入t_user_show
        tTaskService.updateByCorrected(date, null,(qw) -> {
            qw.eq("exam_tran_id",tExamUser.getExamId());
            qw.eq("user_code",tExamUser.getUserId());
            qw.eq("show_type",0);
        });

        return true;
    }

    /**
     * 人工审批
     */
    @Override
    @Transactional
    public Object correct(TExamDto tExamTemplate) {
        if (tExamTemplate.getUser().size() != 1) {
            throw new RuntimeException("用户信息不匹配，提交失败");
        }

        // 自动审题+自动计算成绩+更新t_Exam_question_user
        int score = 0; // 总得分
        List<Object> question = tExamTemplate.getQuestion();
        for (Object item : question) {
            String questionConfigStr = JSON.toJSONString(item);
            // 构建出试题对象
            QuestionConfig questionConfig = QuestionConfigFactory.buildInstance(questionConfigStr);
            // 获取分数
            score += questionConfig.getScoreBySuccess();

            // 更新t_exam_paper_user
            Long examPaperUserId = JSON.parseObject(questionConfigStr).getLong("examPaperUserTableId");
            tExamPaperUserService.updateUserPaperWithCorrected(examPaperUserId,questionConfig);
        }

        // 获取用户信息
        TExamUser tExamUser = tExamTemplate.getUser().get(0);
        // 更新用户信息
        Date date = tExamUserService.updateUserWithCorrected(tExamUser, score);

        // 用户完成考试，接入t_user_show
        tTaskService.updateByCorrected(date, score,(qw) -> {
            qw.eq("exam_tran_id",tExamUser.getExamId());
            qw.eq("user_code",tExamUser.getUserId());
            qw.eq("show_type",0);
        });

        return true;
    }

    // 批改模式获取配置
    public TExamDto examConfigByCorrect(Long examId, Long userId) {
        List<TExamUser> tExamUsers = tExamUserService.getOneUserInfo(examId, userId);
        if(tExamUsers == null || tExamUsers.isEmpty()) {
            throw new RuntimeException("该试卷不存在");
        } else if(tExamUsers.get(0).getPaperDone() == 0) {
            throw new RuntimeException("该试卷未完成答题");
        } else if( tExamUsers.get(0).getCorrected() == 1) {
            throw new RuntimeException("该试卷已批改");
        } else {
            List<TExamCorrector> correctors = tExamCorrectorService.getOneCorrectorInfo(examId, AuthUtil.getUserId());
            if(correctors != null && !correctors.isEmpty()){
                return selectExamConfig(examId,userId);
            }else {
                throw new RuntimeException("无权限进行此操作");
            }
        }
    }
}

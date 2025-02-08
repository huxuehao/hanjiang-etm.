package com.hanjiang.etm.train.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.dto.TTrainingDto;
import com.hanjiang.etm.common.entity.*;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.common.vo.TTrainingVo;
import com.hanjiang.etm.common.dto.QuestionConfig;
import com.hanjiang.etm.question.factory.QuestionConfigFactory;
import com.hanjiang.etm.task.service.TTaskService;
import com.hanjiang.etm.train.mapper.TTrainingMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：培训
 *
 * @author huxuehao
 **/
@Service
public class TTrainingServiceImpl extends ServiceImpl<TTrainingMapper, TTraining> implements TTrainingService {
    private final TTrainingQuestionService tTrainingQuestionService;
    private final TTrainingQuestionUserService tTrainingQuestionUserService;
    private final TTrainingUserService tTrainingUserService;
    private final TTrainingFileService tTrainingFileService;
    private final TTrainingClassifyService tTrainingClassifyService;
    private final TTaskService tTaskService;

    public TTrainingServiceImpl(TTrainingQuestionService tTrainingQuestionService, TTrainingQuestionUserService tTrainingQuestionUserService, TTrainingUserService tTrainingUserService, TTrainingFileService tTrainingFileService, TTrainingClassifyService tTrainingClassifyService, TTaskService tTaskService) {
        this.tTrainingQuestionService = tTrainingQuestionService;
        this.tTrainingQuestionUserService = tTrainingQuestionUserService;
        this.tTrainingUserService = tTrainingUserService;
        this.tTrainingFileService = tTrainingFileService;
        this.tTrainingClassifyService = tTrainingClassifyService;
        this.tTaskService = tTaskService;
    }

    //存在性检查
    private void checkValid(TTrainingDto trainingDto) {
        if (trainingDto.getFile() == null || trainingDto.getFile().isEmpty()) {
            throw new RuntimeException("课件配置不可为空");
        }
        if (trainingDto.getQuestion() == null || trainingDto.getQuestion().isEmpty()) {
            throw new RuntimeException("试题配置不可为空");
        }
        if (trainingDto.getUser() == null || trainingDto.getUser().isEmpty()) {
            throw new RuntimeException("用户配置不可为空");
        }
    }

    @Override
    @Transactional
    public boolean saveV2(TTrainingDto trainingDto) {
        checkValid(trainingDto);
        // 保存主表
        TTraining tTrain = new TTraining();
        BeanUtils.copyProperties(trainingDto, tTrain);
        tTrain.setCreateTime(new Date());
        tTrain.setCreateUser(String.valueOf(AuthUtil.getUserId()));
        save(tTrain);

        // 保存字表
        tTrainingFileService.safeSaveBatch(trainingDto.getFile(), tTrain.getId());
        tTrainingQuestionService.safeSaveBatch(trainingDto.getQuestion(), tTrain.getId());
        tTrainingUserService.safeSaveBatch(trainingDto.getUser(), tTrain.getId());

        return true;
    }

    @Override
    @Transactional
    public boolean updateByIdV2(TTrainingDto trainingDto) {
        TTraining training = getById(trainingDto.getId());
        if (training != null && training.getPublished() == 1) {
            throw new RuntimeException("当前培训已发布，禁止更新配置");
        }
        checkValid(trainingDto);
        // 保存主表
        TTraining tTrain = new TTraining();
        BeanUtils.copyProperties(trainingDto, tTrain);
        updateById(tTrain);

        // 保存字表
        tTrainingFileService.safeSaveBatch(trainingDto.getFile(), tTrain.getId());
        tTrainingQuestionService.safeSaveBatch(trainingDto.getQuestion(), tTrain.getId());
        tTrainingUserService.safeSaveBatch(trainingDto.getUser(), tTrain.getId());

        return true;
    }

    @Override
    @Transactional
    public boolean removeBatchByIdsV2(List<Long> ids) {
        removeBatchByIds(ids);
        tTrainingFileService.deleteByTrainIds(ids);
        tTrainingQuestionService.deleteByTrainIds(ids);
        tTrainingUserService.deleteByTrainIds(ids);
        return true;
    }

    @Override
    public TTrainingVo getByIdV2(Long id) {
        // 查询主表
        TTraining training = getById(id);
        if (training == null) {
            throw new RuntimeException("当前记录已被删除");
        }
        // 生成VO
        TTrainingVo tTrainingVo = new TTrainingVo();
        BeanUtils.copyProperties(training, tTrainingVo);

        // 查询课件字表
        tTrainingVo.setFile(tTrainingFileService.getFilesByTrainId(id));
        // 查询试题字表
        tTrainingVo.setQuestion(tTrainingQuestionService.getObjectByTrainId(id));
        // 查询用户字表
        tTrainingVo.setUser(tTrainingUserService.getUsersByTrainId(id));

        return tTrainingVo;
    }

    @Override
    @Transactional
    public boolean publish(Long id) {
        // 更新主表
        TTraining tTraining = getById(id);
        if (tTraining == null) {
            throw new RuntimeException("当前培训不存在");
        } else if (tTraining.getPublished() == 1) {
            throw new RuntimeException("当前培训已发布，请勿重复操作");
        } else if (checkTrainTimeout(tTraining)) {
            throw new RuntimeException("当前考试已超时，请重新设置结束时间");
        }
        Date publishTime = new Date();
        tTraining.setPublished(1);
        tTraining.setPublishTime(publishTime);
        updateById(tTraining);

        // 获取试题
        List<TTrainingQuestion> tTrainingQuestions = tTrainingQuestionService.getQuestionsByTrainId(id);

        // 获取用户
        List<TTrainingUser> trainingUsers = tTrainingUserService.getUsersByTrainId(id);

        // 生成用户试题，每100条入一次库
        int tag  = 0;
        List<TTrainingQuestionUser> tTrainingQuestionUsers = new ArrayList<>();
        for (TTrainingUser user : trainingUsers) {
            for (TTrainingQuestion question : tTrainingQuestions) {
                TTrainingQuestionUser questionUser = new TTrainingQuestionUser();
                questionUser.setTrainId(id);
                questionUser.setTrainQuestionId(question.getId());
                questionUser.setUserId(user.getUserId());
                questionUser.setConfig(question.getConfig());
                questionUser.setDoAlready(0);
                questionUser.setSort(questionUser.getSort());
                tTrainingQuestionUsers.add(questionUser);
                tag++;
                if (tag % 100 == 0) {
                    tTrainingQuestionUserService.saveBatch(tTrainingQuestionUsers);
                    tTrainingQuestionUsers = new ArrayList<>();
                } else if (tag == (trainingUsers.size() * tTrainingQuestions.size())) {
                    tTrainingQuestionUserService.saveBatch(tTrainingQuestionUsers);
                }
            }

        }

        // 完成培训的发布，接入t_user_show，每100条入一次库
        TTrainingClassify classify = tTrainingClassifyService.getById(tTraining.getClassifyId());
        tTaskService.accessByPublished(
                trainingUsers.stream().map(TTrainingUser::getUserId).collect(Collectors.toList()),
                1,
                (task) -> {
                    task.setExamTranId(tTraining.getId());
                    task.setClassifyId(tTraining.getClassifyId());
                    task.setClassifyName(classify.getName());
                    task.setShowName(tTraining.getTitle());
                    task.setPublishTime(publishTime);
                    task.setLimitStartTime(tTraining.getLimitStartTime());
                    task.setLimitEndTime(tTraining.getLimitEndTime());
                }
        );

        return true;
    }

    @Override
    @Transactional
    public boolean cancelPublish(Long id, int force) {
        if (force == 1) {
            initQuestionUserByTrainId(id);
        } else {
            if (!tTrainingQuestionUserService.haveDoneUserByExamId(id)) {
                initQuestionUserByTrainId(id);
            } else {
                throw new RuntimeException("操作被拒绝，已有用户完成答题");
            }
        }
        // 更新主表
        TTraining tTraining = getById(id);
        tTraining.setPublished(0);
        tTraining.setPublishTime(null);
        updateById(tTraining);

        // 解除培训发布，接入t_user_show
        tTaskService.deleteByMixinsId(tTraining.getId());
        return true;
    }

    // 初始化用户痕迹
    private void initQuestionUserByTrainId(Long id) {
        tTrainingQuestionUserService.deleteByTrainId(id);
        tTrainingUserService.resetUserAnswer(id);
    }

    @Override
    @Transactional
    public TTrainingVo getUserTrainConfig(Long trainId, Long userId) {
        // 查询主表
        TTraining training = getById(trainId);
        // 检测培训是否过期
        if (training == null) {
            throw new RuntimeException("当前记录已被删除");
        } else if (training.getPublished() == 0) {
            throw new RuntimeException("当前培训暂未发布，无法继续培训");
        }

        // 查询用户字表
        List<TTrainingUser> trainingUsers = tTrainingUserService.getOneUserInfo(trainId, userId);
        if (trainingUsers.size() != 1) {
            throw new RuntimeException("匹配失败，从培训中获取到多个用户信息");
        }

        // 过期检查
        if (checkTrainTimeout(training)) {
            if (trainingUsers.get(0).getPaperDone() == 0) {
                throw new RuntimeException("您已错过培训有效期，无法继续培训");
            }
        } else if (checkTrainNotStart(training)) {
            throw new RuntimeException("当前培训未开始，无法继续培训");
        }

        TTrainingVo tTrainingVo = new TTrainingVo();
        BeanUtils.copyProperties(training, tTrainingVo);
        tTrainingVo.setUser(trainingUsers);

        // 查询课件表
        tTrainingVo.setFile(tTrainingFileService.getFilesByTrainId(trainId));

        // 查询试题字表
        List<TTrainingQuestionUser> list = tTrainingQuestionUserService.getOnePaper(trainId, userId);
        List<Object> questions = new LinkedList<>();
        list.forEach(item -> {
            JSONObject questionConfig = JSON.parseObject(item.getConfig());
            questionConfig.put("trainQuestionUserTableId", String.valueOf(item.getId()));
            questions.add(questionConfig);
        });
        tTrainingVo.setQuestion(questions);

        // 判断用户是否完成答题
        if (trainingUsers.get(0).getPaperDone() == 0) {
            tTaskService.updateDoStartTime(trainId, userId, 1);
        }

        return tTrainingVo;
    }

    // 检查培训是否过期
    public boolean checkTrainTimeout(TTraining training) {
        Date limitEndTime = training.getLimitEndTime();
        if (limitEndTime != null) {
            return new Date().getTime() > limitEndTime.getTime();
        }
        throw new RuntimeException("未找到截止时间，请联系管理员");
    }

    // 检查培训是否开始
    public boolean checkTrainNotStart(TTraining training) {
        Date limitStartTime = training.getLimitStartTime();
        if (limitStartTime != null) {
            return new Date().getTime() < limitStartTime.getTime();
        }
        throw new RuntimeException("未找到截止时间，请联系管理员");
    }

    @Override
    @Transactional
    public Object paperAnswer(TTrainingVo body) {
        if (body.getUser().size() != 1) {
            throw new RuntimeException("用户信息不匹配，提交失败");
        }

        // 自动审题+自动计算成绩+更新t_train_question_user
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

            // 更新t_train_question_user
            Long trainQuestionUserTableId = JSON.parseObject(questionConfigStr).getLong("trainQuestionUserTableId");
            tTrainingQuestionUserService.updateUserPaperWithCorrected(trainQuestionUserTableId, questionConfig);
        }

        // 更新审批后的用户信息
        TTrainingUser trainingUser = body.getUser().get(0);
        Date date = tTrainingUserService.updateUserWithCorrected(trainingUser, score);

        // 用户完成培训，接入t_user_show
        tTaskService.updateByCorrected(date, score,(qw) -> {
            qw.eq("exam_tran_id", trainingUser.getTrainId());
            qw.eq("user_code", trainingUser.getUserId());
            qw.eq("show_type", 1);
        });

        return getUserTrainConfig(trainingUser.getTrainId(), trainingUser.getUserId());
    }
}

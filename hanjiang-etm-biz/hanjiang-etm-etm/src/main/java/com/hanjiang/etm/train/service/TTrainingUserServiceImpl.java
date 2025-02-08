package com.hanjiang.etm.train.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.entity.TTrainingUser;
import com.hanjiang.etm.train.mapper.TTrainingUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 描述：培训用户
 *
 * @author huxuehao
 **/
@Service
public class TTrainingUserServiceImpl extends ServiceImpl<TTrainingUserMapper, TTrainingUser> implements TTrainingUserService {

    @Override
    @Transactional
    public void safeSaveBatch(List<TTrainingUser> user, Long trainId) {
        QueryWrapper<TTrainingUser> qw = new QueryWrapper<>();
        qw.eq("train_id", trainId);
        remove(qw);

        // 保存
        for (TTrainingUser tTrainingUser : user) {
            tTrainingUser.setTrainId(trainId);
            tTrainingUser.setPaperDone(0);
        }
        saveBatch(user);
    }

    @Override
    public void deleteByTrainIds(List<Long> trainIds) {
        QueryWrapper<TTrainingUser> qw = new QueryWrapper<>();
        qw.in("train_id", trainIds);
        remove(qw);
    }

    @Override
    public List<TTrainingUser> getUsersByTrainId(Long trainId) {
        QueryWrapper<TTrainingUser> qw = new QueryWrapper<>();
        qw.eq("train_id", trainId);
        return list(qw);
    }

    @Override
    public void resetUserAnswer(Long trainId) {
        UpdateWrapper<TTrainingUser> uw = new UpdateWrapper<>();
        uw.set("paper_done", 0);
        uw.set("score", 0);
        uw.set("plat", null);
        uw.set("do_time", null);
        uw.eq("train_id", trainId);
        update(uw);
    }

    @Override
    public  List<TTrainingUser> getOneUserInfo(Long trainId, Long userId) {
        QueryWrapper<TTrainingUser> qw = new QueryWrapper<>();
        qw.eq("train_id", trainId);
        qw.eq("user_id", userId);
        return list(qw);
    }

    @Override
    public Date updateUserWithCorrected(TTrainingUser trainingUser, Integer score) {
        // 更新答题平台
        Date date = new Date();
        UpdateWrapper<TTrainingUser> uw = new UpdateWrapper<>();
        if (!"WEB".equals(trainingUser.getPlat())) {
            trainingUser.setPlat("APP");
        }
        uw.set("plat", trainingUser.getPlat());
        uw.eq("train_id",trainingUser.getTrainId());
        uw.eq("user_id",trainingUser.getUserId());
        uw.set("do_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        uw.set("paper_done",1);
        uw.set("score",score);
        update(uw);

        return date;
    }
}

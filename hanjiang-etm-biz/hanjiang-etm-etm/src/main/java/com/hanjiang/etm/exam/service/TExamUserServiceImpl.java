package com.hanjiang.etm.exam.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.dto.TDDInfoDto;
import com.hanjiang.etm.common.entity.TExamUser;
import com.hanjiang.etm.exam.mapper.TExamUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 描述：考试-用户
 *
 * @author huxuehao
 **/
@Service
public class TExamUserServiceImpl extends ServiceImpl<TExamUserMapper, TExamUser> implements TExamUserService {
    @Override
    @Transactional
    public void safeSaveBatch(Long examId, List<TExamUser> users) {
        // 删除
        QueryWrapper<TExamUser> qw = new QueryWrapper<>();
        qw.eq("exam_id", examId);
        remove(qw);

        // 批量保存
        if (users != null && !users.isEmpty()) {
            users.forEach(item -> {
                item.setExamId(examId);
                item.setPaperDone(0);
            });
            saveBatch(users);
        }

    }

    @Override
    public List<TExamUser> selectListByExamId(Long examId) {
        QueryWrapper<TExamUser> qw = new QueryWrapper<>();
        qw.eq("exam_id", examId);
        return  list(qw);
    }

    @Override
    public void resetUserAnswer(Long examId) {
        UpdateWrapper<TExamUser> uw = new UpdateWrapper<>();
        uw.set("corrected", 0);
        uw.set("paper_done", 0);
        uw.set("score", 0);
        uw.set("plat", null);
        uw.set("do_time", null);
        uw.eq("exam_id", examId);
        update(uw);
    }

    @Override
    public void deleteByExamId(Long examId) {
        UpdateWrapper<TExamUser> uw = new UpdateWrapper<>();
        uw.eq("exam_id", examId);
        remove(uw);
    }

    @Override
    public List<TExamUser> getOneUserInfo(Long examId, Long userId) {
        QueryWrapper<TExamUser> qw = new QueryWrapper<>();
        qw.eq("exam_id", examId);
        qw.eq("user_id", userId);
        return list(qw);
    }

    @Override
    public TExamUser saveUserInfoOfApp(TDDInfoDto tddInfoDto) {
        TExamUser tExamUser = new TExamUser();
        tExamUser.setExamId(tddInfoDto.getExamId());
        tExamUser.setUserId(Long.valueOf(tddInfoDto.getUserId()));
        tExamUser.setUserName(tddInfoDto.getUserName());
        tExamUser.setUserCode(tddInfoDto.getUserCode());
        tExamUser.setOrgName(tddInfoDto.getOrgName());
        tExamUser.setPaperDone(0);
        tExamUser.setCorrected(0);
        save(tExamUser);

        return tExamUser;
    }

    @Override
    public Date updateUserWithCorrected(TExamUser tExamUser, Integer score) {
        Date date = new Date();
        UpdateWrapper<TExamUser> tExamUserUw = new UpdateWrapper<>();
        tExamUserUw.eq("exam_id",tExamUser.getExamId());
        tExamUserUw.eq("user_id",tExamUser.getUserId());
        tExamUserUw.set("do_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        tExamUserUw.set("paper_done",1);

        // 更新答题平台
        if (!"WEB".equals(tExamUser.getPlat())) {
            tExamUser.setPlat("APP");
        }
        tExamUserUw.set("plat", tExamUser.getPlat());

        if (score == null) {
            tExamUserUw.set("corrected",0);
        } else {
            tExamUserUw.set("corrected",1);
            tExamUserUw.set("score",score);
        }
        update(tExamUserUw);
        return date;
    }
}

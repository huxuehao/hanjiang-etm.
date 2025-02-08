package com.hanjiang.etm.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.entity.TTask;
import com.hanjiang.etm.common.function.AccessFunction;
import com.hanjiang.etm.common.function.QueryWrapperFunction;
import com.hanjiang.etm.common.mp.support.PageParams;

import java.util.Date;
import java.util.List;

public interface TTaskService extends IService<TTask> {

     /**
      * 用于查询待考试/已考试、待培训/已培训的数据
      */
     List<TTask> list(TTask tUserShow,String activeType);

     /**
      * 用于查询我的考试、我的培训、我的审批数据（分页）
      */
     IPage<TTask> page(TTask tUserShow, PageParams pageParams, String activeType, Integer status);

     /**
      * 发布后接入
      * @param userIds 用户ID集合
      * @param type    类型（0考试，1培训）
      * @param access  自定义函数接口，其目的是让接入方自行设置动态的必要参数
      */
     void accessByPublished(List<Long> userIds, int type, AccessFunction access);

     /**
      * 删除考试/培训记录
      * @param mixinsId 考试/ID培训ID
      */
     void deleteByMixinsId(Long mixinsId);

     /**
      * 更新用户考试时间
      * @param mixinsId 考试/ID培训ID
      * @param userId   用户ID
      * @param type     类型（0考试，1培训）
      */
     void updateDoStartTime(Long mixinsId, Long userId, int type);

     /**
      * 审批后更新记录信息
      * @param date  日期
      * @param score 分数
      * @param queryWrapperFunction 自定义函数接口，用于设置查询条件
      */
     void updateByCorrected(Date date, Integer score, QueryWrapperFunction queryWrapperFunction);
}

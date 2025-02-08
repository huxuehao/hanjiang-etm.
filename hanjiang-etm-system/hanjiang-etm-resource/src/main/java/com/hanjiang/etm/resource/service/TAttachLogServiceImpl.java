package com.hanjiang.etm.resource.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.entity.TAttachLog;
import com.hanjiang.etm.resource.mapper.TAttachLogMapper;
import org.springframework.stereotype.Service;

/**
 * 描述：附件操作日志
 *
 * @author huxuehao
 **/
@Service
public class TAttachLogServiceImpl extends ServiceImpl<TAttachLogMapper, TAttachLog> implements TAttachLogService {
}

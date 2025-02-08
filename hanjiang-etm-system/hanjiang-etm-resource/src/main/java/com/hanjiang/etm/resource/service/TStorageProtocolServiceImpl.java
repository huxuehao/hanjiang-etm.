package com.hanjiang.etm.resource.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.entity.TStorageProtocol;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.common.util.Func;
import com.hanjiang.etm.resource.mapper.TStorageProtocolMapper;
import com.hanjiang.etm.resource.storage.core.FileStorageService;
import com.hanjiang.etm.resource.storage.core.ProtocolType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 描述：文件存储协议配置
 *
 * @author huxuehao
 **/
@Service
public class TStorageProtocolServiceImpl extends ServiceImpl<TStorageProtocolMapper, TStorageProtocol> implements TStorageProtocolService {
    @Override
    public boolean saveV2(TStorageProtocol body) {
        if(body.getValid() == 1) {
            QueryWrapper<TStorageProtocol> qw = new QueryWrapper<>();
            qw.eq("valid", 1);
            qw.eq("del_flag", 0);
            qw.ne("id", body.getId());
            List<TStorageProtocol> validList = list(qw);
            if(Func.isNotEmpty(validList)) {
                body.setValid(0);
            }
        }
        return save(body);
    }

    @Override
    public boolean updateV2(TStorageProtocol body) {
        if(body.getValid() == 1) {
            QueryWrapper<TStorageProtocol> qw = new QueryWrapper<>();
            qw.eq("valid", 1);
            qw.eq("del_flag", 0);
            qw.ne("id", body.getId());
            List<TStorageProtocol> validList = list(qw);
            if(Func.isNotEmpty(validList)) {
                throw new RuntimeException("已存在有效的协议配置");
            }
        }

        UpdateWrapper<TStorageProtocol> uw = new UpdateWrapper<>();
        uw.eq("id", body.getId());
        uw.set("name", body.getName());
        uw.set("protocol", body.getProtocol());
        uw.set("valid", body.getValid());
        uw.set("remark", body.getRemark());
        uw.set("update_user", String.valueOf(AuthUtil.getUserId()));
        uw.set("update_time", new Date());

        TStorageProtocol oldData = getById(body);
        if (!oldData.getProtocol().equals(body.getProtocol())) {
            uw.set("protocol_config", null);
        }

        return update(uw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean validSuccess(Long id) {
        TStorageProtocol protocol = getById(id);
        if (protocol.getValid() == 1) {
            return true;
        }
        UpdateWrapper<TStorageProtocol> uw = new UpdateWrapper<>();
        uw.set("valid", 0);
        update(uw);

        uw.clear();
        uw.set("valid", 1);
        uw.eq("id", id);
        return update(uw);
    }

    @Override
    public FileStorageService getStorageService() {
        TStorageProtocol storageProtocol = getCurrentValidProtocol();
        return createStorageService(storageProtocol);
    }

    /**
     * 获取当前有效的协议
     */
    private TStorageProtocol getCurrentValidProtocol() {
        LambdaQueryWrapper<TStorageProtocol> qw = Wrappers
                .<TStorageProtocol>lambdaQuery()
                .eq(TStorageProtocol::getValid, 1)
                .eq(TStorageProtocol::getDelFlag, 0);
        List<TStorageProtocol> list = list(qw);
        if (Func.isEmpty(list) || list.size() > 1) {
            throw new RuntimeException("存储配置不存咋唯一一个有效的配置");
        }
        return list.get(0);
    }

    /**
     * 创建存储服务
     * @param storageProtocol 存储协议实体
     */
    private FileStorageService createStorageService(TStorageProtocol storageProtocol) {
        String protocolConfig = storageProtocol.getProtocolConfig();
        if (Func.isEmpty(protocolConfig)) {
            throw new RuntimeException("请完善协议配置");
        }

        return FileStorageService.create(ProtocolType.valueOf(storageProtocol.getProtocol()), protocolConfig);
    }
}

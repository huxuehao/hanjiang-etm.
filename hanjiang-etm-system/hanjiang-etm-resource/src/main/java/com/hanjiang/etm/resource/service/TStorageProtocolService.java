package com.hanjiang.etm.resource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanjiang.etm.common.entity.TStorageProtocol;
import com.hanjiang.etm.resource.storage.core.FileStorageService;

/**
 * 描述：文件存储协议配置
 *
 * @author huxuehao
 **/
public interface TStorageProtocolService extends IService<TStorageProtocol> {
    boolean saveV2(TStorageProtocol body);
    boolean updateV2(TStorageProtocol body);

    /**
     * 根据ID设置启用
     * @param id id
     */
    boolean validSuccess(Long id);

    /**
     * 获取存储服务
     */
    FileStorageService getStorageService();


}

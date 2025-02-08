package com.hanjiang.etm.auth.service;

/**
 * 描述：API接口扫描
 *
 * @author huxuehao
 **/
public interface ApiEndpointService {
    /**
     * 扫描并报错API接口信息
     */
    void scanAndSaveApiEndpoints();
}

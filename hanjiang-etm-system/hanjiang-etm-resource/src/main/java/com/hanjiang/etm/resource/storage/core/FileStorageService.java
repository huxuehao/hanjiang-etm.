package com.hanjiang.etm.resource.storage.core;

import com.alibaba.fastjson2.JSON;
import com.hanjiang.etm.resource.storage.config.AmazonS3Config;
import com.hanjiang.etm.resource.storage.config.FtpStorageConfig;
import com.hanjiang.etm.resource.storage.config.LocalStorageConfig;
import com.hanjiang.etm.resource.storage.core.service.AmazonS3StorageService;
import com.hanjiang.etm.resource.storage.core.service.FtpFileStorageService;
import com.hanjiang.etm.resource.storage.core.service.LocalFileStorageService;

import java.io.InputStream;
import java.util.LinkedList;

/**
 * 描述：
 *
 * @author huxuehao
 **/
public interface FileStorageService {
    /**
     * 获取存储协议
     */
    String getProtocol();

    /**
     * 保存文件
     *
     * @param inputStream 文件输入流
     * @param path        文件相对路径
     */
    void save(InputStream inputStream, String path);

    /**
     * 保存文件分片
     * @param inputStream 当前分片的文件输入流
     * @param chunkPath   当前分片存储路径
     */
    void saveChunk(InputStream inputStream, String chunkPath);

    /**
     * 合并文件分片
     * @param filePath   完整文件存储路径
     * @param chunkPaths 分片路径有序集合
     */
    void mergeChunk(String filePath, LinkedList<String> chunkPaths);

    /**
     * 根据路径删除
     *
     * @param path 文件路径或url
     */
    void delete(String path);

    /**
     * 根据path加载
     *
     * @param path 文件路径或url
     */
    InputStream load(String path);

    static FileStorageService create(ProtocolType protocol, String protocolConfig) {
        FileStorageService storageService;
        switch (protocol) {
            case FTP:
                storageService = new FtpFileStorageService(JSON.parseObject(protocolConfig, FtpStorageConfig.class));
                break;
            case LOCAL:
                storageService =  new LocalFileStorageService(JSON.parseObject(protocolConfig, LocalStorageConfig.class));
                break;
            case S3:
                storageService =  new AmazonS3StorageService(JSON.parseObject(protocolConfig, AmazonS3Config.class));
                break;
            default:
                storageService = null;
        }

        if (storageService == null) {
            throw new RuntimeException("协议匹配失败");
        }

        return storageService;
    }
}

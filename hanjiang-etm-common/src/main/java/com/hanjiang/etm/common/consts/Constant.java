package com.hanjiang.etm.common.consts;

/**
 * 描述：
 *
 * @author huxuehao
 **/
public class Constant {
    /* token秘钥 */
    public static final String JWT_SECRET = "120606C95258FF3A3ED51603A683C8B1";
    public static final String LOGIN_USER_KEY = "LOGIN-USER-KEY";
    public static final String API_AUTH_CACHE_PRE = "user:api:auth:code:";
    /* token存活时间（ms）*/
    public static final Long TOKEN_LIVE_TIME = 10800000L;
    /* 允许上传的文件类型*/
    public static final String ALLOW_UPLOAD_FILE_TYPE = ".doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.txt,.mp4,.jpg,.jpeg,.png,.gif,.webp";
    /* 允许上传的最大文件大小（MB）*/
    public static final String SINGLE_FILE_MAX_SIZE = "300";
    public static final String SALT = "33453459278276B306C3E5E05A371FF9";
}

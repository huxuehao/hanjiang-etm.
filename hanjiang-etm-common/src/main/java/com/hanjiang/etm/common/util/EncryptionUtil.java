package com.hanjiang.etm.common.util;

import cn.hutool.core.lang.UUID;
import com.hanjiang.etm.common.consts.Constant;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 描述：加密工具类
 *
 * @author huxuehao
 **/
public class EncryptionUtil {

    /**
     * 使用默认的加盐值进行MD5加密
     * @param inputPass  原始密码
     * @return 加密后的字符串
     */
    public static String md5(String inputPass) {
        String str = String.valueOf(Constant.SALT.charAt(0)) + Constant.SALT.charAt(2) + inputPass + Constant.SALT.charAt(5) + Constant.SALT.charAt(4);
        return DigestUtils.md5Hex(str);
    }

    /**
     * 使用自定义的盐值进行MD5加密
     * @param inputPass  原始密码
     * @param salt 盐值
     * @return 加密后的字符串
     */
    public static String md5(String inputPass, String salt) {
        String str = String.valueOf(salt.charAt(0)) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return DigestUtils.md5Hex(str);
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-","");
    }
}

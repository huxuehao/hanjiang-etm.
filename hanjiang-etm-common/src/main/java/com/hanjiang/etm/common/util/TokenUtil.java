package com.hanjiang.etm.common.util;

import com.hanjiang.etm.common.consts.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 描述：token工具类
 *
 * @author huxuehao
 **/
public class TokenUtil {
    /**
     * 创建token
     * @param id        ID
     * @param subject   主题
     * @param ttlMillis 过期时间
     */
    public static String createToken(String id, String subject, long ttlMillis) {
        /* 指定签名的时候使用的签名算法 */
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        /* token生成的时间 */
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        /* 设置claims */
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constant.LOGIN_USER_KEY, UUID.randomUUID());
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(signatureAlgorithm, generalKey());
        /* 设置过期时间 */
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        /* 返回token */
        return builder.compact();
    }

    /**
     * 解密token
     * @param token token
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(generalKey())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 由字符串生成加密key
     * @return 秘钥
     */
    public static SecretKey generalKey(){
        /* 本地配置文件中加密的密文 */
        byte[] encodedKey = Base64.decodeBase64(Constant.JWT_SECRET);
        /* 根据给定的字节数组使用AES加密算法构造一个密钥。*/
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }
}

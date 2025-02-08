package com.hanjiang.etm.user.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.common.consts.Constant;
import com.hanjiang.etm.common.dto.TUserDto;
import com.hanjiang.etm.common.entity.LoginBody;
import com.hanjiang.etm.common.entity.TUser;
import com.hanjiang.etm.common.exception.NotAuthException;
import com.hanjiang.etm.common.util.*;
import com.hanjiang.etm.params.core.ParamsAdapter;
import com.hanjiang.etm.user.mapper.TUserMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：用户
 *
 * @author huxuehao
 **/
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService {
    private final CacheUtil cacheUtil;
    private final ParamsAdapter paramsAdapter;

    public TUserServiceImpl(CacheUtil cacheUtil, ParamsAdapter paramsAdapter) {
        this.cacheUtil = cacheUtil;
        this.paramsAdapter = paramsAdapter;
    }

    @Override
    public TUserDto validateLogin(LoginBody body) {
        /* 验证用户名和密码 */
        List<TUser> users = list(new QueryWrapper<TUser>().eq("account", body.getAccount()));
        if (users.size() > 1) {
            throw new RuntimeException("用户验证失败");
        }
        if (Func.isEmpty(users)) {
            throw new RuntimeException("用户不存在");
        }
        TUser user = users.get(0);
        if (!user.getPassword().equals(EncryptionUtil.md5(body.getPassword()))) {
            throw new RuntimeException("或密码错误");
        }

        return getTUserDto(user);
    }

    private TUserDto getTUserDto(TUser user) {
        String uuid = EncryptionUtil.uuid();

        /* 获取系统参数 */
        String tokenTTL = paramsAdapter.getValue("TOKEN_LIVE_TIME");

        /* 存活时间 */
        long tokenLiveTime = Long.parseLong(tokenTTL);
        long refTokenLiveTime = tokenLiveTime + (1000 * 60 * 60);

        /* 生成token */
        String accessToken = TokenUtil.createToken(uuid, JSON.toJSONString(user), tokenLiveTime);
        String refreshToken = TokenUtil.createToken(uuid + "_refresh", JSON.toJSONString(user), refTokenLiveTime);

        // 补充用户信息
        TUserDto tUserDto = new TUserDto();
        BeanUtils.copyProperties(user, tUserDto);
        tUserDto.setAccessToken(accessToken);
        tUserDto.setRefreshToken(refreshToken);
        //cacheUtil.set(uuid, accessToken, tokenLiveTime);

        return tUserDto;
    }

    @Override
    public void logout(String id) {
        try {
            cacheUtil.del(Constant.API_AUTH_CACHE_PRE + AuthUtil.getUserId());
            cacheUtil.del(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("缓存服务异常");
        }
    }

    @Override
    public TUserDto refreshToken(String refreshToken) {
        /* 解析Token */
        Claims claims;
        try {
            claims = TokenUtil.parseToken(refreshToken);
        } catch (Exception e) {
            throw new NotAuthException();
        }

        // 判断是否超时
        if (claims.getExpiration().getTime() < System.currentTimeMillis()) {
            cacheUtil.del(claims.getId());
            throw new NotAuthException("refreshToken无效");
        }

        TUser user = JSON.parseObject(claims.getSubject(), TUser.class);
        TUser user__ = getById(user.getId());
        if (user__ == null) {
            throw new NotAuthException("用户不存在");
        }

        return getTUserDto(user__);
    }
}

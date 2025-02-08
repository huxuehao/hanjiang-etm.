package com.hanjiang.etm.common.auth;

import com.hanjiang.etm.common.exception.NotAuthException;
import com.hanjiang.etm.common.util.BeanUtil;
import com.hanjiang.etm.common.util.CacheUtil;
import com.hanjiang.etm.common.util.Func;
import com.hanjiang.etm.common.util.TokenUtil;
import io.jsonwebtoken.Claims;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 描述：权限认证的
 *
 * @author huxuehao
 **/
public class AuthInterceptor implements HandlerInterceptor {
    CacheUtil cacheUtil = BeanUtil.getBean(CacheUtil.class);

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        /* 如果不是映射到方法中，那么直接通过 */
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        /* 获取映射的方法 */
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        /* 判断是否跳过认证 */
        if (method.isAnnotationPresent(PassAuth.class)) {
            return true;
        }

        /* 获取请求头中的 token */
        String token = request.getHeader("Authorization");
        if(Func.isEmpty(token)) {
            token = request.getParameter("Authorization");
            if (Func.isEmpty(token)) {
                throw new NotAuthException();
            }
        }

        try {
            /* 验证Token，token错误或过期则会报错 */
            Claims claims = TokenUtil.parseToken(token);
        } catch (Exception e) {
            throw new NotAuthException();
        }

        //// 判断是否超时
        //if (claims.getExpiration().getTime() < System.currentTimeMillis()) {
        //    cacheUtil.del(claims.getId());
        //    throw new NotAuthException();
        //}
        //
        //Object cacheToken;
        //try {
        //    /* 从缓存中获取token */
        //    cacheToken = cacheUtil.get(claims.getId());
        //} catch (Exception e) {
        //    throw new RuntimeException("缓存服务异常");
        //}
        //if (Func.isEmpty(cacheToken)) {
        //    throw new NotAuthException();
        //}

        return true;
    }
}

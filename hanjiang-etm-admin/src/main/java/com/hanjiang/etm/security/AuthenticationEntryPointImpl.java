package com.hanjiang.etm.security;


import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.common.util.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 *
 * @author huxuehao
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable
{
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
    {
        int code = HttpStatus.UNAUTHORIZED.value();
        String msg = "接口 " + request.getRequestURI()+" 认证失败";
        WebUtil.renderJson(response, R.fail(code, msg));
    }
}

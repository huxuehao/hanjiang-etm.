package com.hanjiang.etm.common.util;

import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 描述：
 *
 * @author huxuehao
 **/
public class WebUtil extends WebUtils {
    private static final Logger log = LoggerFactory.getLogger(WebUtil.class);
    private static final String[] IP_HEADER_NAMES = new String[]{"x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
    private static final Predicate<String> IP_PREDICATE = (ip) -> Func.isEmpty(ip) || "unknown".equalsIgnoreCase(ip);

    @Nullable
    public static String getCookieVal(String name) {
        HttpServletRequest request = getRequest();
        Assert.notNull(request, "request from RequestContextHolder is null");
        return getCookieVal(request, name);
    }

    @Nullable
    public static String getCookieVal(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }

    public static void removeCookie(HttpServletResponse response, String key) {
        setCookie(response, key, (String)null, 0);
    }

    public static void setCookie(HttpServletResponse response, String name, @Nullable String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : ((ServletRequestAttributes)requestAttributes).getRequest();
    }

    public static void renderJson(HttpServletResponse response, Object result) {
        renderJson(response, result, "application/json");
    }

    public static void renderJson(HttpServletResponse response, Object result, String contentType) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(contentType);

        try {
            PrintWriter out = response.getWriter();
            Throwable var4 = null;

            try {
                out.append(JSON.toJSONString(result));
            } catch (Throwable var14) {
                var4 = var14;
                throw var14;
            } finally {
                if (out != null) {
                    if (var4 != null) {
                        try {
                            out.close();
                        } catch (Throwable var13) {
                            var4.addSuppressed(var13);
                        }
                    } else {
                        out.close();
                    }
                }

            }
        } catch (IOException var16) {
            log.error(var16.getMessage(), var16);
        }

    }

    public static String getIP() {
        return getIP(getRequest());
    }

    @Nullable
    public static String getIP(@Nullable HttpServletRequest request) {
        if (request == null) {
            return "";
        } else {
            String ip = null;
            for (String ipHeader : IP_HEADER_NAMES) {
                ip = request.getHeader(ipHeader);
                if (!IP_PREDICATE.test(ip)) {
                    break;
                }
            }

            if (IP_PREDICATE.test(ip)) {
                ip = request.getRemoteAddr();
            }

            if (Func.isEmpty(ip)) {
                return null;
            } else {
                return ip.split(",")[0].trim();
            }
        }
    }

    public static String getHeader(String name) {
        HttpServletRequest request = getRequest();
        return Objects.requireNonNull(request).getHeader(name);
    }

    public static Enumeration<String> getHeaders(String name) {
        HttpServletRequest request = getRequest();
        return Objects.requireNonNull(request).getHeaders(name);
    }

    public static Enumeration<String> getHeaderNames() {
        HttpServletRequest request = getRequest();
        return Objects.requireNonNull(request).getHeaderNames();
    }

    public static String getParameter(String name) {
        HttpServletRequest request = getRequest();
        return Objects.requireNonNull(request).getParameter(name);
    }

    public static String getRequestBody(ServletInputStream servletInputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(servletInputStream, StandardCharsets.UTF_8));

            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException var16) {
            var16.printStackTrace();
        } finally {
            if (servletInputStream != null) {
                try {
                    servletInputStream.close();
                } catch (IOException var15) {
                    var15.printStackTrace();
                }
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException var14) {
                    var14.printStackTrace();
                }
            }

        }

        return sb.toString();
    }
}

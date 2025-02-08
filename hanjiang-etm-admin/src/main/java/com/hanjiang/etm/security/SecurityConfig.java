package com.hanjiang.etm.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring-security配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthenticationEntryPointImpl unauthorizedHandler;
    private final JwtAuthenticationTokenFilter authenticationTokenFilter;

    public SecurityConfig(AuthenticationEntryPointImpl unauthorizedHandler, JwtAuthenticationTokenFilter authenticationTokenFilter) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.authenticationTokenFilter = authenticationTokenFilter;
    }

    // 配置安全策略
    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 禁用HTTP响应标头
                .headers().cacheControl().disable().and()
                // 认证失败处理类
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 无状态会话管理，基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();
        // 添加JWT filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    //protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
    //    httpSecurity
    //            // CSRF禁用，因为不使用session
    //            .csrf().disable()
    //            // 禁用HTTP响应标头
    //            .headers().cacheControl().disable().and()
    //            // 认证失败处理类
    //            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
    //            // 无状态会话管理，基于token，所以不需要session
    //            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
    //            // 过滤请求
    //            .authorizeRequests()
    //            // 对于登录login, 允许匿名访问
    //            .antMatchers("/user/login").permitAll()
    //            // 静态资源，可匿名访问
    //            .antMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/profile/**").permitAll()
    //            .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs", "/druid/**").permitAll()
    //            // 除上面外的所有请求全部需要鉴权认证
    //            .anyRequest().authenticated()
    //            .and()
    //            .headers().frameOptions().disable();
    //    // 添加Logout filter
    //    //httpSecurity.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
    //    // 添加JWT filter
    //    httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    //    // 添加CORS filter
    //    //httpSecurity.addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class);
    //    //httpSecurity.addFilterBefore(corsFilter, LogoutFilter.class);
    //
    //    return httpSecurity.build();
    //}
}

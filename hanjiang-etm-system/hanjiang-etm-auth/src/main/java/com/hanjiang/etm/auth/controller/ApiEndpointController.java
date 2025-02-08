package com.hanjiang.etm.auth.controller;

import com.hanjiang.etm.auth.service.ApiEndpointService;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.consts.Constant;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.common.util.CacheUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：接口采集/校验
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/endpoint")
@MenuTag(code = "system_apimanager")
public class ApiEndpointController {
    private final ApiEndpointService apiEndpointService;
    private final CacheUtil cacheUtil;

    public ApiEndpointController(ApiEndpointService apiEndpointService, CacheUtil cacheUtil) {
        this.apiEndpointService = apiEndpointService;
        this.cacheUtil = cacheUtil;
    }

    @PreAuthorize("@ps.hasPermission('get::endpoint:scan-and-save-api')")
    @GetMapping(value = "/scan-and-save-api", name = "接口采集/校验")
    public R<?> scanAndSaveApi() {
        apiEndpointService.scanAndSaveApiEndpoints();
        cacheUtil.del(Constant.API_AUTH_CACHE_PRE + "1111111111111111111");
        return R.data(true);
    }
}

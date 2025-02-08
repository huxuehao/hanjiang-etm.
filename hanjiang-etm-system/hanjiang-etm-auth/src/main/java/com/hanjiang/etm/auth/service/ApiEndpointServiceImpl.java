package com.hanjiang.etm.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.entity.TMenu;
import com.hanjiang.etm.common.entity.TMenuApi;
import com.hanjiang.etm.menu.service.TMenuService;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 描述：API接口扫描
 *
 * @author huxuehao
 **/
@Service
public class ApiEndpointServiceImpl implements ApiEndpointService {
    private final ApplicationContext applicationContext;
    private final TMenuService menuService;
    private final TMenuApiService menuApiService;

    public ApiEndpointServiceImpl(ApplicationContext applicationContext, TMenuService menuService, TMenuApiService menuApiService) {
        this.applicationContext = applicationContext;
        this.menuService = menuService;
        this.menuApiService = menuApiService;
    }

    @Override
    public void scanAndSaveApiEndpoints() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(Controller.class);

        Set<String> notExistApiCodes = new HashSet<>();

        // 获取菜单code的map, code为key, id为value
        List<TMenu> menuList = menuService.list();
        Map<String, Long> menuCodeIdMap = menuList.stream()
                .filter(item -> item.getDelFlag() == 0)
                .collect(Collectors.toMap(TMenu::getCode, TMenu::getId));

        for (Object controller : controllers.values()) {
            Class<?> clazz = controller.getClass();

            MenuTag menuTagAnnotation = clazz.getAnnotation(MenuTag.class);
            if (menuTagAnnotation == null) {
                /*
                 * 避免CGLIB代理无法获取到目标对象的注解。
                 * 如果此处获取的对象是通过CGLIB实现的代理对象，那么获取其父类就能获取到真实的对象，
                 * 因为CGLIB是通过继承实现目标对象的代理的。
                 */
                Class<?> superclass = clazz.getSuperclass();
                if ((menuTagAnnotation = superclass.getAnnotation(MenuTag.class)) == null) {
                    continue;
                }
                clazz = superclass;
            }

            // 获取菜单code
            String menuCode = menuTagAnnotation.code();
            if (menuCodeIdMap.get(menuCode) == null) {
                continue;
            }

            RequestMapping classMapping = clazz.getAnnotation(RequestMapping.class);
            String basePath = (classMapping ==  null? "" : classMapping.value()[0]);

            // 解析Method，生成TMenuApi实体
            List<TMenuApi> menuApis = new ArrayList<>();
            for (Method method : clazz.getDeclaredMethods()) {
                TMenuApi menuApi = processMethod(method, basePath);
                if (menuApi == null) {
                    continue;
                }
                menuApi.setMenuId(menuCodeIdMap.get(menuCode));
                menuApi.setNextTimeMillis(currentTimeMillis);
                menuApis.add(menuApi);
            }

            if (menuApis.isEmpty()) {
                continue;
            }

            QueryWrapper<TMenuApi> qw = new QueryWrapper<>();
            qw.eq("menu_id", menuApis.get(0).getMenuId());
            qw.eq("type", 1);
            qw.ne("next_time_millis", currentTimeMillis);
            qw.eq("del_flag", 0);
            List<TMenuApi> list = menuApiService.list(qw);

            // 获得 code 和 id 的 map
            Map<String, Long> currentExistApiMap = list.stream().collect(Collectors.toMap(TMenuApi::getCode, TMenuApi::getId));

            // 插入更新操作
            Set<String> currentExistApiCodes = new HashSet<>(currentExistApiMap.keySet());;
            currentExistApiCodes.addAll(notExistApiCodes);
            for (TMenuApi menuApi : menuApis) {
                if (currentExistApiCodes.contains(menuApi.getCode())) {
                    menuApi.setId(currentExistApiMap.get(menuApi.getCode()));
                    menuApiService.updateById(menuApi);
                } else {
                    menuApiService.save(menuApi);
                }
                // 移除已经处理过的Api
                currentExistApiCodes.remove(menuApi.getCode());
            }

            //记录未处理的API
            notExistApiCodes.clear();
            notExistApiCodes.addAll(currentExistApiCodes);
        }

        //删除未处理的API
        if (!notExistApiCodes.isEmpty()) {
            QueryWrapper<TMenuApi> menuApiQueryWrapper = new QueryWrapper<>();
            menuApiQueryWrapper.in("code", notExistApiCodes);
            menuApiQueryWrapper.in("type", 1);
            menuApiQueryWrapper.in("del_flag", 0);
            menuApiService.remove(menuApiQueryWrapper);
        }
    }

    /**
     * 解析方法
     * @param method   方法
     * @param basePath 基础路径
     */
    private TMenuApi processMethod(Method method, String basePath) {
        PreAuthorize permission = method.getAnnotation(PreAuthorize.class);
        if (permission == null) {
            return null;
        }

        GetMapping getAnno = method.getAnnotation(GetMapping.class);
        PostMapping postAnno = method.getAnnotation(PostMapping.class);
        PutMapping putAnno = method.getAnnotation(PutMapping.class);
        DeleteMapping deleteAnno = method.getAnnotation(DeleteMapping.class);
        RequestMapping allAnno = method.getAnnotation(RequestMapping.class);

        String httpMethod = null;
        String path = null;
        String name = null;

        if (getAnno != null) {
            httpMethod = "GET";
            path = getAnno.value().length > 0 ? getAnno.value()[0] : "";
            name = getAnno.name();
        } else if (postAnno != null) {
            httpMethod = "POST";
            path = postAnno.value().length > 0 ? postAnno.value()[0] : "";
            name = postAnno.name();
        } else if (putAnno != null) {
            httpMethod = "PUT";
            path = putAnno.value().length > 0 ? putAnno.value()[0] : "";
            name = putAnno.name();
        } else if (deleteAnno != null) {
            httpMethod = "DELETE";
            path = deleteAnno.value().length > 0 ? deleteAnno.value()[0] : "";
            name = deleteAnno.name();
        } else if (allAnno != null) {
            httpMethod = "ALL";
            path = allAnno.value().length > 0 ? allAnno.value()[0] : "";
            name = allAnno.name();
        }

        if (httpMethod != null && !path.isEmpty()) {
            String fullPath = basePath + path;
            String code = parseCode(permission);

            TMenuApi api = new TMenuApi();
            api.setCode(code);
            api.setName(name);
            api.setPath(fullPath);
            api.setMethod(httpMethod);
            api.setType(1);

            return api;
        }

        return null;
    }

    /**
     * 解析code
     * @param permission @PreAuthorize注解
     */

    private String parseCode(PreAuthorize permission) {
        String value = permission.value();
        Pattern pattern = Pattern.compile("'(.*?)'"); // 匹配单引号之间的内容
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new IllegalArgumentException("权限编号解析失败");
        }
    }

    /**
     * 生成Api接口的code
     * @param httpMethod 方法
     * @param path       路径
     */
    private String generateCode(String httpMethod, String path) {
        return httpMethod.toLowerCase() + "_" + path.replace("/", "_").replace("-", "_").replace("{", "_").replace("}", "");
    }
}

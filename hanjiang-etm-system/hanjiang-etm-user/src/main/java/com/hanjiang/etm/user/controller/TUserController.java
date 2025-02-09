package com.hanjiang.etm.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hanjiang.etm.common.auth.MenuTag;
import com.hanjiang.etm.common.auth.PassAuth;
import com.hanjiang.etm.common.dto.RePwdDto;
import com.hanjiang.etm.common.entity.LoginBody;
import com.hanjiang.etm.common.entity.TUser;
import com.hanjiang.etm.common.mp.support.MP;
import com.hanjiang.etm.common.mp.support.PageParams;
import com.hanjiang.etm.common.r.R;
import com.hanjiang.etm.common.util.AuthUtil;
import com.hanjiang.etm.common.util.EncryptionUtil;
import com.hanjiang.etm.common.util.Func;
import com.hanjiang.etm.user.service.TUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：用户
 *
 * @author huxuehao
 **/
@RestController
@RequestMapping("/user")
@MenuTag(code = "system_user")
public class TUserController {
    private final TUserService userService;

    public TUserController(TUserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("@ps.hasPermission('post::user:add')")
    @PostMapping(value = "/add", name = "新增")
    public R<?> add(@RequestBody TUser body) {
        if ("administrator".equals(body.getCode())) {
            return R.fail(202, "超级管理员角色限制新增");
        }
        QueryWrapper<TUser> qw = new QueryWrapper<>();
        qw.eq("code", body.getCode());
        qw.or().eq("account", body.getAccount());
        List<TUser> list = userService.list(qw);
        if (list == null || list.isEmpty()) {
            body.setId(null);
            body.setPassword(EncryptionUtil.md5(body.getPassword()));
            return R.data(userService.save(body));
        } else if(list.get(0).getCode().equals(body.getCode())) {
            return R.fail("用户编码已存在");
        } else if(list.get(0).getAccount().equals(body.getAccount())) {
            return R.fail("登录账号已存在");
        } else {
            return R.fail("未知字段重复");
        }
    }

    @PreAuthorize("@ps.hasPermission('post::user:re-pwd')")
    @PostMapping(value = "/re-pwd", name = "重置登录密码")
    public R<?> rePwd(@RequestBody RePwdDto body) {
        TUser user = userService.getById(AuthUtil.getUserId());

        if (!EncryptionUtil.md5(body.getOldPwd()).equals(user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        user.setPassword(EncryptionUtil.md5(body.getNewPwd()));
        return R.data(userService.updateById(user));
    }

    @PreAuthorize("@ps.hasPermission('post::user:delete')")
    @PostMapping(value = "/delete", name = "删除")
    public R<?> delete(@RequestBody List<Long> ids) {
        if (ids.contains(1111111111111111111L)) {
            return R.fail(202, "超级管理员限制删除");
        }
        return R.data(userService.removeBatchByIds(ids));
    }

    @PreAuthorize("@ps.hasPermission('post::user:valid')")
    @PostMapping(value = "/valid", name = "解封账号")
    public R<?> valid(@RequestBody List<Long> ids) {
        UpdateWrapper<TUser> uw = new UpdateWrapper<>();
        uw.in("id", ids);
        uw.set("valid", 1);
        return R.data(userService.update(uw));
    }

    @PreAuthorize("@ps.hasPermission('post::user:resetPwd')")
    @PostMapping(value = "/resetPwd", name = "重置密码")
    public R<?> resetPwd(@RequestBody List<Long> ids) {
        UpdateWrapper<TUser> uw = new UpdateWrapper<>();
        uw.in("id", ids);
        uw.set("password", EncryptionUtil.md5("Hanj@user123.com"));
        userService.update(uw);
        return R.fail(202, "初始密码Hanj@user123.com");
    }

    @PreAuthorize("@ps.hasPermission('post::user:update')")
    @PostMapping(value = "/update", name = "更新")
    public R<?> update(@RequestBody TUser body) {
        if (body.getId() == 1111111111111111111L || "administrator".equals(body.getCode())) {
            TUser admin = userService.getById(body.getId());
            admin.setEmail(body.getEmail());
            admin.setNickname(body.getNickname());
            admin.setPhone(body.getPhone());
            admin.setOrgId(body.getOrgId());
            userService.updateById(admin);
            return R.fail(202, "超级管理员部分信息限制修改");
        } else {
            QueryWrapper<TUser> qw = new QueryWrapper<>();
            qw.eq("code", body.getCode());
            qw.or().eq("account", body.getAccount());
            qw.ne("id", body.getId());
            List<TUser> list = userService.list(qw);
            if(list == null || list.isEmpty() || list.get(0).getId().equals(body.getId())) {
                return R.data(userService.updateById(body));
            } else if(list.get(0).getCode().equals(body.getCode())) {
                return R.fail("用户编码已存在");
            } else if(list.get(0).getAccount().equals(body.getAccount())) {
                return R.fail("登录账号已存在");
            } else {
                return R.fail("未知字段重复");
            }
        }
    }

    @PreAuthorize("@ps.hasPermission('get::user:page')")
    @GetMapping(value = "/page", name = "分页")
    public R<?> tree(TUser user, PageParams pageParams) {
        return R.data(userService.page(MP.getPage(pageParams),MP.getQueryWrapper(user)));
    }

    @PreAuthorize("@ps.hasPermission('get::user:selectOne')")
    @GetMapping(value = "/selectOne", name = "根据ID唯一获取")
    public R<?> selectOne(@RequestParam Long id) {
        return R.data(userService.getById(id));
    }

    @PreAuthorize("@ps.hasPermission('get::user:current-user-info')")
    @GetMapping(value = "/current-user-info", name = "获取当前用户的用户信息")
    public R<?> currentUserInfo() {
        return R.data(userService.getById(AuthUtil.getUserId()));
    }

    @PreAuthorize("@ps.hasPermission('post::user:selectByIds')")
    @PostMapping(value = "/selectByIds", name = "根据ID集合批量获取")
    public R<?> selectByIds(@RequestBody List<Long> ids) {
        return R.data(userService.listByIds(ids));
    }

    @PassAuth
    @PreAuthorize("@ps.hasPermission('post::user:login')")
    @PostMapping(value = "/login", name = "登录")
    public R<?> login(@RequestBody LoginBody body) {
        return R.data(userService.validateLogin(body));
    }

    @PreAuthorize("@ps.hasPermission('get::user:logout')")
    @GetMapping(value = "/logout", name = "退出")
    public R<?> login(@RequestParam Long id) {
        userService.logout(String.valueOf(id));
        return R.success("OK");
    }

    @PassAuth
    @PreAuthorize("@ps.hasPermission('get::user:refresh-token')")
    @GetMapping(value = "/refresh-token", name = "刷新Token")
    public R<?> login(@RequestParam String refreshToken) {
        if (Func.isEmpty(refreshToken)) {
            throw new RuntimeException("缺少必要的请求参数");
        }
        return R.data(userService.refreshToken(refreshToken));
    }
}

package com.hanjiang.etm.auth.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanjiang.etm.auth.mapper.TRoleAuthMapper;
import com.hanjiang.etm.common.entity.TRoleAuth;
import org.springframework.stereotype.Service;

/**
 * 描述：角色权限
 *
 * @author huxuehao
 **/
@Service
public class TRoleAuthServiceImpl extends ServiceImpl<TRoleAuthMapper, TRoleAuth> implements TRoleAuthService {
}

package com.hanjiang.etm.params.core.impl;

import com.hanjiang.etm.common.consts.Constant;
import com.hanjiang.etm.common.util.Func;
import com.hanjiang.etm.params.core.ParamsAdapter;
import com.hanjiang.etm.params.core.ParamsCore;
import org.springframework.stereotype.Component;

/**
 * 描述：token存活时长
 *
 * @author huxuehao
 **/
@Component
public class TokenTTLParams implements ParamsCore {
    @Override
    public String checkAndFormatValue(String value) {
        if(Func.isEmpty(value)) {
            throw new RuntimeException("value值不可为空");
        }
        String trim = value.trim();
        try {
            Long.parseLong(trim);
        } catch (NumberFormatException e) {
            throw new RuntimeException("系统参数TOKEN_LIVE_TIME不合法",e);
        }
        return trim;
    }

    @Override
    public String getDefaultValue() {
        return String.valueOf(Constant.TOKEN_LIVE_TIME);
    }

    @Override
    public void register(ParamsAdapter adapter) {
        adapter.register("TOKEN_LIVE_TIME", new TokenTTLParams());
    }
}

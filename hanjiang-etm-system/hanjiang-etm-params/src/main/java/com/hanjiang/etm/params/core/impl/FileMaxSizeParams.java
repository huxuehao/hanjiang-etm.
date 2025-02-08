package com.hanjiang.etm.params.core.impl;

import com.hanjiang.etm.common.consts.Constant;
import com.hanjiang.etm.common.util.Func;
import com.hanjiang.etm.params.core.ParamsAdapter;
import com.hanjiang.etm.params.core.ParamsCore;
import org.springframework.stereotype.Component;

/**
 * 描述：允许最大上传文件大小（MB）
 *
 * @author huxuehao
 **/
@Component
public class FileMaxSizeParams implements ParamsCore {
    @Override
    public String checkAndFormatValue(String value) {
        if(Func.isEmpty(value)) {
            throw new RuntimeException("value值不可为空");
        }
        String trim = value.trim();
        try {
            Integer.parseInt(trim);
        } catch (NumberFormatException e) {
            throw new RuntimeException("系统参数SINGLE_FILE_MAX_SIZE不合法",e);
        }
        return trim;
    }

    @Override
    public String getDefaultValue() {
        return Constant.SINGLE_FILE_MAX_SIZE;
    }

    @Override
    public void register(ParamsAdapter adapter) {
        adapter.register("SINGLE_FILE_MAX_SIZE", new FileMaxSizeParams());
    }
}

package com.hanjiang.etm.params.core.impl;

import com.hanjiang.etm.common.consts.Constant;
import com.hanjiang.etm.common.util.Func;
import com.hanjiang.etm.params.core.ParamsAdapter;
import com.hanjiang.etm.params.core.ParamsCore;
import org.springframework.stereotype.Component;

/**
 * 描述：允许上传的文件类型
 *
 * @author huxuehao
 **/
@Component
public class AllowUploadFile implements ParamsCore {
    @Override
    public String checkAndFormatValue(String value) {
        if(Func.isEmpty(value)) {
            throw new RuntimeException("value值不可为空");
        }
        String trim = value.trim();
        trim = trim.replace(" ", "");
        trim = trim.replace("，", ",");
        trim = trim.replace("、", ",");
        trim = trim.replace(";", ",");
        trim = trim.replace("；", ",");
        return trim;
    }

    @Override
    public String getDefaultValue() {
        return Constant.ALLOW_UPLOAD_FILE_TYPE;
    }

    @Override
    public void register(ParamsAdapter adapter) {
        adapter.register("ALLOW_UPLOAD_FILE_TYPE", new AllowUploadFile());
    }
}

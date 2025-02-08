package com.hanjiang.etm.common.r;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author huxuehao
 */
public interface IResultCode extends Serializable {
    String getMessage();

    int getCode();
}

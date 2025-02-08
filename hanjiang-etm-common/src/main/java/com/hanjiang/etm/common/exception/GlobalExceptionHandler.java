package com.hanjiang.etm.common.exception;

import com.hanjiang.etm.common.r.R;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 描述：全局异常处理器
 *
 * @author huxuehao
 **/

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotAuthException.class)
    public ResponseEntity<?> notAuthExceptionHandler(NotAuthException e) {
        Throwable cause = e.getCause();
        if (cause != null) {
            return new ResponseEntity<>(cause.getMessage(), HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ExceptionHandler(value = Exception.class)
    public R<?> exceptionHandler(Exception e) {
        e.printStackTrace();

        Throwable cause = e.getCause();
        if (cause != null) {
            return R.fail(500, cause.getMessage());
        } else {
            return R.fail(500, e.getMessage());
        }
    }
}

package org.loopring.mobi.exception;

import org.loopring.mobi.dto.ResponseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-19 3:44 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public ResponseResult exceptionHandler(Exception e) {
        return ResponseResult.generateResult(false, e.getLocalizedMessage());
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseResult runtimeExceptionHandler(RuntimeException e) {
        return ResponseResult.generateResult(false, e.getLocalizedMessage());
    }
}

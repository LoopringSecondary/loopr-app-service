package org.loopring.mobi.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-11-19 3:37 PM
 * Cooperation: loopring.org 路印协议基金会
 */
@Data
@Builder
public class ResponseResult<T> {

    private Boolean success;

    private T message;

    public static <T> ResponseResult generateResult(Boolean success, T message) {
        return ResponseResult.builder().success(success).message(message).build();
    }

    public static ResponseResult generateResult(Boolean success) {
        return ResponseResult.builder().success(success).message("").build();
    }

    public static <T> ResponseResult generateResult(T message) {
        return ResponseResult.builder().success(true).message(message).build();
    }
}

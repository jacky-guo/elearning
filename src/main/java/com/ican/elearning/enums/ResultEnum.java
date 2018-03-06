package com.ican.elearning.enums;

import lombok.Getter;

/**
 * Created by JackyGuo
 * 2017/9/2 17:33
 */
@Getter
public enum ResultEnum {

    PARAM_ERROR(40001,"參數不正確"),
    PARAM_EMPTY_ERROR(40002,"參數不完整"),
    PYTHONSERVER_ERROR(40003,"python服務器異常"),
    NOT_EXIST_ERROR(40004,"內容不存在"),
    ACCOUNTORPASSWORD_ERROR(40005,"賬號或密碼不正確"),
    ACCOUNT_EXIST(40006,"賬號已被註冊"),
    UPLOAD_ERROR(5,"上傳錯誤"),
    BADREQUEST(400,"錯誤的請求"),
    UNAUTHORIZED(401,"用戶驗證錯誤"),
    FORBIDDEN(403,"服務器拒絕請求"),
    NOTFOUND(404,"請求失敗，內容不存在")
    ;
    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}


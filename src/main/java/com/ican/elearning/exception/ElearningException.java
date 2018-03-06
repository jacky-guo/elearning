package com.ican.elearning.exception;

import com.ican.elearning.enums.ResultEnum;

/**
 * Created by JackyGuo
 * 2017/9/2 17:32
 */
public class ElearningException extends RuntimeException {
    //錯誤碼
    private Integer code;

    public ElearningException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public ElearningException(Integer code,String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}

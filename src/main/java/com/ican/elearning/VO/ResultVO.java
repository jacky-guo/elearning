package com.ican.elearning.VO;

import lombok.Data;

/**
 * http請求返回的最外層對象
 * Created by JackyGuo
 * 2017/9/1 16:56
 */
@Data
public class ResultVO<T>{
    //錯誤碼
    private Integer code; //0表示成功,
    //提示信息
    private String msg = "";
    //具體內容
    private T data;
}

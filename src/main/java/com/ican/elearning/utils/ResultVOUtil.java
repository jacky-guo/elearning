package com.ican.elearning.utils;

import com.ican.elearning.VO.ResultVO;

/**
 * Created by JackyGuo
 * 2017/9/1 17:30
 */
public class ResultVOUtil {
    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setMsg("成功");
        resultVO.setCode(0);
        return resultVO;
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code,String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}

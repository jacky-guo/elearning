package com.ican.elearning.utils;

import java.util.Random;

/**
 * Created by JackyGuo
 * 2017/9/1 15:47
 */
public class KeyUtil {
    /**
     * 生成唯一的主鍵
     * 格式：時間+隨機數
     * @return
     */

    public static synchronized String genUniqueKey() {
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(number);

    }
}

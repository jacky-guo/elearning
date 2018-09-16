package com.ican.elearning.utils;

import com.ican.elearning.dto.QuestionDTO;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class CommonUtil {
    public static void randomSet(int min, int max, int n, HashSet<Integer> set) {
        if (n > (max - min + 1) || max < min) {
            return;
        }
        for (int i = 0; i < n; i++) {
            // 调用Math.random()方法
            int num = (int) (Math.random() * (max - min)) + min;
            set.add(num);// 将不同的数存入HashSet中
        }
        int setSize = set.size();
        // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
        if (setSize < n) {
            randomSet(min, max, n - setSize, set);// 递归
        }
    }

    public static List<QuestionDTO> questionSort(List<QuestionDTO> questionDTOList) {
        Collections.sort(questionDTOList, new Comparator<QuestionDTO>() {
            @Override
            public int compare(QuestionDTO o1, QuestionDTO o2) {
                if (o1.getQuestionLevel() < o2.getQuestionLevel()) {
                    return -1;
                }else {
                    return 1;
                }
            }
        });
        return questionDTOList;
    }

}

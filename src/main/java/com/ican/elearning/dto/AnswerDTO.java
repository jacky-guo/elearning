package com.ican.elearning.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JackyGuo
 * 2017/9/17 23:59
 */
@Data
public class AnswerDTO {
//    private Integer answerOrders;
//    private String title;
//    private String rightvalue;
//    private String wrongvalue1;
//    private String wrongvalue2;
//    private String wrongvalue3;
//    private String seletvalue;

    private Integer answerOrders;
    private String title;
    private List<String> answerContent = new ArrayList<>();
    private String correct;
}

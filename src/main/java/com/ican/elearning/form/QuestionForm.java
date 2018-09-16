package com.ican.elearning.form;

import lombok.Data;

/**
 * Created by JackyGuo
 * 2017/9/17 22:54
 */
@Data
public class QuestionForm {

    private Integer questionId;
    //題目難易度
    private Integer questionLevel;
    //題目類型
    private Integer questionType;
    //題目是否經過專家確認
    private Integer questionAutoCreate;
    //題目來源教材編號
    private Integer questionParagraphId;
    //題目內容
    private String questionContent;
    //題目所屬年級
    private String questionGrade;
    //題目來源
    private String questionSource;
    //題目標籤
    private String questionHashtag;
    private String createBy;
    private String updateBy;
    private String answerDTOList;

}

package com.ican.elearning.form;

import lombok.Data;

/**
 * Created by JackyGuo
 * 2017/9/17 22:54
 */
@Data
public class QuestionForm {
    private String questionGrade;
    private Integer questionType;
    private String questionHashtag;
    private String questionContent;
    private Integer questionParagraphId;
    private String createBy;
    private String ans;

}

package com.ican.elearning.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JackyGuo
 * 2017/10/4 1:15
 */
@Data
public class QuestionDTO {
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
    private Date createTime;
    private Date updateTime;
    private List<AnswerDTO> answerDTOList = new ArrayList<>();
}

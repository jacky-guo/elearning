package com.ican.elearning.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JackyGuo
 * 2017/10/3 23:07
 */
@Data
public class ExamDTO {
    private Integer examId;
    //試卷標題或者說明
    private String examTitle;
    //試卷難易度
    private Integer examLevel;
    //試卷類型
    private Integer examType;
    //試卷所屬年級
    private String examGrade;
    //試卷題目數量
    private Integer examNumber;
    //試卷標籤
    private String examHashtag;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;
    private List<QuestionDTO> questionDTOList = new ArrayList<>();
}

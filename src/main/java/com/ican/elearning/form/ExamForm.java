package com.ican.elearning.form;

import lombok.Data;

/**
 * Created by JackyGuo
 * 2017/10/8 17:54
 */
@Data
public class ExamForm {
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
    private String questionDTOList;

}

package com.ican.elearning.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by JackyGuo
 * 2017/10/3 22:36
 */
@Entity
@Data
@DynamicUpdate  //自動更新時間
public class Exam {
    @Id //主鍵
    @GeneratedValue //autoincrement
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
}

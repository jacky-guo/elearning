package com.ican.elearning.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Date;

/**
 * Created by JackyGuo
 * 2017/10/4 1:21
 */

@Entity
@Data
@DynamicUpdate  //自動更新時間
@IdClass(ExamQuestionMultiKeys.class)
public class ExamQuestion {
    @Id //主鍵
    private Integer examId;
    @Id //複合主鍵
    private Integer questionId;

    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

}

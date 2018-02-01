package com.ican.elearning.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by JackyGuo
 * 2017/9/4 17:12
 */
@Entity
@Data
@DynamicUpdate  //自動更新時間
public class Question {

    @Id //主鍵
    @GeneratedValue //autoincrement
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

}

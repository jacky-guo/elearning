package com.ican.elearning.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by JackyGuo
 * 2017/9/17 23:49
 */
@Entity
@Data
@DynamicUpdate  //自動更新時間
public class Answer {
    @Id //主鍵
    @GeneratedValue //autoincrement
    private Integer answerId;
    //題目ID
    private Integer questionId;
    //答案編號或者答案的問題
    private String answerTitle;
    //正確答案(用於自動出題參考)
    private String answerKeyword;
    //答案內容
    private String answerContent;
    //答案序號
    private Integer answerOrders;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;


}

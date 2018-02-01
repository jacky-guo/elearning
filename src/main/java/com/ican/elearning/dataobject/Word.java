package com.ican.elearning.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by JackyGuo
 * 2017/9/1 14:16
 */
@Entity
@Data
@DynamicUpdate  //自動更新時間
public class Word {

    @Id //主鍵
    @GeneratedValue //autoincrement
    private Integer wordId;
    //單字難易度
    private Integer wordLevel; //not null
    //單字長度
    private Integer wordLength; //not null
    //單字內容
    private String wordContent; //not null
    //單字詞性
    private String wordPartofspeech;
    //單字中文意思
    private String wordInterpretation;
    //單字所屬年級
    private String wordGrade;
    //單字來源
    private String wordSource;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;
}

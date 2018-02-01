package com.ican.elearning.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by JackyGuo
 * 2017/9/4 2:19
 */
@Entity
@Data
@DynamicUpdate  //自動更新時間
public class Paragraph {

    @Id //主鍵
    @GeneratedValue //autoincrement
    private Integer paragraphId;
    //教材難易度
    private Integer paragraphLevel;
    //教材單字數
    private Integer paragraphWordCount;
    //教材句子數
    private Integer paragraphSentenceLength;
    //教材內容
    private String paragraphContent;
    //教材標籤
    private String paragraphHashtag;
    //教材所屬年級
    private String paragraphGrade;
    //教材來源
    private String paragraphSource;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;
}

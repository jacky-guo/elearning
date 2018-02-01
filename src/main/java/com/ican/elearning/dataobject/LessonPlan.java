package com.ican.elearning.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by JackyGuo
 * 2017/9/12 18:22
 */
@Entity
@Data
@DynamicUpdate  //自動更新時間
public class LessonPlan {

    @Id //主鍵
    @GeneratedValue //autoincrement
    private Integer planId;
    //教案檔名
    private String planFilename;
    //教案本地地址
    private String planFileurl;
    //教案所屬年級
    private String planGrade;
    //教案所屬單元
    private String planLesson;
    //教案標籤
    private String planHashtag;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

}

package com.ican.elearning.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by JackyGuo
 * 2017/9/1 15:23
 */
@Entity
@Data
@DynamicUpdate  //自動更新時間
public class User {
    @Id
    private String userId;
    //使用者賬號
    private String userAccount;
    //使用者密碼
    private String userPassword;
    //使用者姓名
    private String userName;
    //使用者Email
    private String userEmail;
    //使用者所屬年級
    private String userGrade;
    //使用者所屬學校
    private String userSchool;
    //使用者排名
    private String userRank;
    //使用者興趣
    private String interest;
    //使用者程度
    private Integer userLevel;
    //使用者身份
    private Integer userIdentity;
    //使用者最後登錄時間
    private Date lastloginTime;

}

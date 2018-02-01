package com.ican.elearning.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by JackyGuo
 * 2017/9/2 18:07
 */
@Data
public class UserForm {

    @NotEmpty(message = "賬號必填")
    private String username;

    @NotEmpty(message = "密碼必填")
    private String password;

//    @NotEmpty(message = "等級必填")
//    private Integer userLevel;
//
//    @NotEmpty(message = "身份必填")
//    private Integer userIdentity;

}

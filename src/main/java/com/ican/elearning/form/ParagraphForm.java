package com.ican.elearning.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by JackyGuo
 * 2017/9/4 15:30
 */
@Data
public class ParagraphForm {

    private Integer paragraphId;
    private Integer paragraphLevel;
    @NotEmpty(message = "教材內容必填")
    private String paragraphContent;
    private String paragraphHashtag;
    @NotEmpty(message = "教材所屬年級必填")
    private String paragraphGrade;
    private String paragraphSource;
    private String createBy;
    private String updateBy;
}

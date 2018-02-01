package com.ican.elearning.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by JackyGuo
 * 2017/9/3 2:34
 */
@Data
public class WordForm {
    private Integer wordId;
    private Integer wordLevel;
    @NotEmpty(message = "單字必填")
    private String wordContent;

    @NotEmpty(message = "詞性必填")
    private String wordPartofspeech;

    private String wordInterpretation;

    @NotEmpty(message = "年級必填")
    private String wordGrade;
    private String createBy;
    private String updateBy;
    private String wordSource;


}

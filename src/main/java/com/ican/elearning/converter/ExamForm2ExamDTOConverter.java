package com.ican.elearning.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ican.elearning.dto.ExamDTO;
import com.ican.elearning.dto.QuestionDTO;
import com.ican.elearning.enums.ResultEnum;
import com.ican.elearning.exception.ElearningException;
import com.ican.elearning.form.ExamForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JackyGuo
 * 2017/10/9 0:10
 */
@Slf4j
public class ExamForm2ExamDTOConverter {
    public static ExamDTO convert(ExamForm examForm) {

        Gson gson = new Gson();

        ExamDTO examDTO = new ExamDTO();
        BeanUtils.copyProperties(examForm,examDTO);

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        try {
            questionDTOList = gson.fromJson(examForm.getQuestionDTOList(),
                    new TypeToken<List<QuestionDTO>>() {
                    }.getType());
        } catch (Exception e) {
            log.error("【對象轉換】錯誤，string={}",examForm.getQuestionDTOList());
            throw new ElearningException(ResultEnum.PARAM_ERROR);
        }

        examDTO.setQuestionDTOList(questionDTOList);
        return examDTO;
    }


}

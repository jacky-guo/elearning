package com.ican.elearning.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ican.elearning.dto.AnswerDTO;
import com.ican.elearning.dto.QuestionDTO;
import com.ican.elearning.form.QuestionForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class QuestionForm2QuestionDTOConverter {

    public static QuestionDTO convert(QuestionForm questionForm) {

        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(questionForm,questionDTO);

        Gson gson = new Gson();
        List<AnswerDTO> answerDTOList = new ArrayList<>();
        try {
            answerDTOList = gson.fromJson(questionForm.getAnswerDTOList(),
                    new TypeToken<List<AnswerDTO>>() {
                    }.getType());
        } catch (Exception e) {
            log.error("【對象轉換】錯誤，string={}",questionForm.getAnswerDTOList());
        }

        questionDTO.setAnswerDTOList(answerDTOList);
        return questionDTO;
    }

}

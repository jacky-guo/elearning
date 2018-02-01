package com.ican.elearning.service;

import com.ican.elearning.dataobject.Paragraph;
import com.ican.elearning.dataobject.Question;
import com.ican.elearning.dto.QuestionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by JackyGuo
 * 2017/9/4 17:48
 */

public interface QuestionService {
    //查詢所有題目
    List<Question> findAll();
    //新增題目/修改題目
    Question save(Question question);
    //查詢題目詳情
    Question findOne(Integer questionId);

    List<QuestionDTO> autoGenerateQuestion(String questionGrade, Integer questionNumber);
    Page<Question> findByQuestionGrade(String questionGrade, Pageable pageable);

}

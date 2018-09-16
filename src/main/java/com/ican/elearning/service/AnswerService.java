package com.ican.elearning.service;

import com.ican.elearning.dataobject.Answer;
import com.ican.elearning.dto.AnswerDTO;

import java.util.List;

/**
 * Created by JackyGuo
 * 2017/9/18 1:47
 */
public interface AnswerService {

//    List<Answer> findByQuestionId(Integer questionId);
    Answer save(Answer answer);
    List<AnswerDTO> findByQuestionId(Integer questionId);
    void delete(Integer questionId);
}

package com.ican.elearning.service;

import com.ican.elearning.dataobject.Answer;

import java.util.List;

/**
 * Created by JackyGuo
 * 2017/9/18 1:47
 */
public interface AnswerService {
    List<Answer> findByQuestionId(Integer questionId);

    Answer save(Answer answer);
}

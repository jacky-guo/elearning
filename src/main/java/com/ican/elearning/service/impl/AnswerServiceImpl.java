package com.ican.elearning.service.impl;

import com.ican.elearning.Dao.AnswerDao;
import com.ican.elearning.dataobject.Answer;
import com.ican.elearning.service.AnswerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by JackyGuo
 * 2017/9/18 1:48
 */
@Slf4j
@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerDao answerDao;

    @Override
    public Answer save(Answer answer) {
        return answerDao.save(answer);
    }

    @Override
    public List<Answer> findByQuestionId(Integer questionId) {
        return answerDao.findByQuestionId(questionId);
    }
}

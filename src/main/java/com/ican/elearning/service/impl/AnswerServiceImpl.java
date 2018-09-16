package com.ican.elearning.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ican.elearning.Dao.AnswerDao;
import com.ican.elearning.dataobject.Answer;
import com.ican.elearning.dto.AnswerDTO;
import com.ican.elearning.service.AnswerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

//    @Override
//    public List<Answer> findByQuestionId(Integer questionId) {
//        return answerDao.findByQuestionId(questionId);
//    }

    @Override
    public List<AnswerDTO> findByQuestionId(Integer questionId) {

        List<AnswerDTO> answerDTOList = new ArrayList<>();
        List<Answer> answerList = answerDao.findByQuestionId(questionId);
        Gson gson = new Gson();
        for (Answer answer:answerList) {
            AnswerDTO answerDTO = new AnswerDTO();
            try {
                answerDTO = gson.fromJson(answer.getAnswerContent(),
                        new TypeToken<AnswerDTO>() {
                        }.getType());
            } catch (Exception e) {
                log.error("【對象轉換】錯誤，string={}",answer.getAnswerContent());
            }
            answerDTOList.add(answerDTO);
        }

        return answerDTOList;
    }

    @Override
    public void delete(Integer questionId) {
        List<Answer> answerList = answerDao.findByQuestionId(questionId);
        for (Answer answer:answerList) {
            answerDao.delete(answer.getAnswerId());
        }
    }
}

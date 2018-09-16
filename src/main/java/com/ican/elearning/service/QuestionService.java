package com.ican.elearning.service;

import com.ican.elearning.dataobject.Paragraph;
import com.ican.elearning.dataobject.Question;
import com.ican.elearning.dataobject.User;
import com.ican.elearning.dto.QuestionDTO;
import lombok.extern.slf4j.Slf4j;
import org.python.antlr.op.In;
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
    //存入資料庫（新增題目/修改題目）
    Question save(Question question);
    //按ID查詢題目
    Question findOne(Integer questionId);
    QuestionDTO question2QuestionDTO(Question question);
    //按年級查詢題目
    Page<Question> findByQuestionGrade(String questionGrade, Pageable pageable);
    List<Question> findByQuestionGrade(String questionGrade);
    //按難易度區間查詢題目
    List<QuestionDTO> findByQuestionLevelBetween(Integer levelLower,Integer levelUpper);
    List<Question> findByQuestionLevelBetweenOrderByQuestionLevelDesc(Integer levelLower,Integer levelUpper);
    //自動產生題目
    List<QuestionDTO> autoGenerateQuestion(String questionGrade, Integer questionNumber);

    QuestionDTO insert(QuestionDTO questionDTO);
    QuestionDTO findByQuestionId(Integer questionId);
    QuestionDTO interactiveQuestion(User user, Integer lastLevel, String result);

    void delete(Integer questionId);

    }

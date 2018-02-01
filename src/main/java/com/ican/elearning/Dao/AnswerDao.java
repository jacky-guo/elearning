package com.ican.elearning.Dao;

import com.ican.elearning.dataobject.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by JackyGuo
 * 2017/9/18 1:45
 */
public interface AnswerDao extends JpaRepository<Answer,Integer> {
    List<Answer> findByQuestionId(Integer questionId);

}

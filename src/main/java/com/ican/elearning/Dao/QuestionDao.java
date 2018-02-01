package com.ican.elearning.Dao;

import com.ican.elearning.dataobject.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by JackyGuo
 * 2017/9/4 17:31
 */
public interface QuestionDao extends JpaRepository<Question,Integer>{
    Page<Question> findByQuestionGrade(String questionGrade, Pageable pageable);

}

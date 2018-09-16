package com.ican.elearning.Dao;

import com.ican.elearning.dataobject.ExamQuestion;
import com.ican.elearning.dataobject.ExamQuestionMultiKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamQuestionDao extends JpaRepository<ExamQuestion,ExamQuestionMultiKeys> {
    List<ExamQuestion> findByExamId(Integer examId);
}

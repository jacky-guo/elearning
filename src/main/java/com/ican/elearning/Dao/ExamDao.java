package com.ican.elearning.Dao;

import com.ican.elearning.dataobject.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by JackyGuo
 * 2017/10/4 5:07
 */
public interface ExamDao extends JpaRepository<Exam,Integer> {
    List<Exam> findByExamGradeAndExamNumber(String examGrade, Integer examNumber);

}

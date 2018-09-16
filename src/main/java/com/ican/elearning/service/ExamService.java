package com.ican.elearning.service;

import com.ican.elearning.dataobject.Exam;
import com.ican.elearning.dataobject.User;
import com.ican.elearning.dto.ExamDTO;

import java.util.List;

/**
 * Created by JackyGuo
 * 2017/10/4 5:08
 */
public interface ExamService {

    List<Exam> findByExamGradeAndExamNumber(String examGrade, Integer examNumber);
    ExamDTO autoGenerateExam(String examGrade, Integer examNumber);
    ExamDTO generateExam(String examGrade, Integer examNumber);
    ExamDTO findExam(Integer examLevel);
    ExamDTO exam2ExamDTO(Exam exam);
    ExamDTO generalExam(String examGrade, Integer examNumber);
    Exam save(Exam exam);
    ExamDTO dynamicExam(User user, Integer lastLevel, String result);

}

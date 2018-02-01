package com.ican.elearning.service;

import com.ican.elearning.dataobject.Exam;
import com.ican.elearning.dto.ExamDTO;

/**
 * Created by JackyGuo
 * 2017/10/4 5:08
 */
public interface ExamService {

    ExamDTO autoGenerateExam(String examGrade,Integer examNumber);
    Exam save(Exam exam);

}

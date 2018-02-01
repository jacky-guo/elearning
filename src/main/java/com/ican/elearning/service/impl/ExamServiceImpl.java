package com.ican.elearning.service.impl;

import com.ican.elearning.dataobject.Exam;
import com.ican.elearning.dto.ExamDTO;
import com.ican.elearning.dto.QuestionDTO;
import com.ican.elearning.service.ExamService;
import com.ican.elearning.service.QuestionService;
import com.ican.elearning.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by JackyGuo
 * 2017/10/4 5:12
 */
@Slf4j
@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ExamService examService;

    @Override
    public ExamDTO autoGenerateExam(String examGrade, Integer examNumber) {

        ExamDTO examDTO = new ExamDTO();
        try {
            List<QuestionDTO> questionDTOList = questionService.autoGenerateQuestion(examGrade, examNumber);
            //TODO 試卷難度和試卷產生者和試卷標題
            Integer examLevel = 0;
            for (QuestionDTO questionDTO :questionDTOList) {
                examLevel += questionDTO.getQuestionLevel();
            }
            examLevel /= questionDTOList.size();
//        examDTO.setExamLevel(Integer.valueOf(examGrade) * 100);
            examDTO.setExamLevel(examLevel);
            examDTO.setCreateBy("AutoGenerate");
            examDTO.setExamTitle("AutoGenerateExam");

            String examHashtag = "";
            for(QuestionDTO questionDTO:questionDTOList) {
                examHashtag = examHashtag + questionDTO.getQuestionHashtag() + ";";
            }

            examDTO.setExamHashtag(examHashtag);
            examDTO.setExamNumber(questionDTOList.size());
            examDTO.setExamGrade(examGrade);
            examDTO.setQuestionDTOList(questionDTOList);

        } catch (Exception e) {
            log.error("【自動產生考卷】自動產生考題錯誤，error={}", e);
        }

        return examDTO;
    }

    @Override
    public Exam save(Exam exam) {
        return examService.save(exam);
    }
}

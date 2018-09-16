package com.ican.elearning.service.impl;

import com.ican.elearning.Dao.ExamDao;
import com.ican.elearning.Dao.ExamQuestionDao;
import com.ican.elearning.dataobject.Exam;
import com.ican.elearning.dataobject.ExamQuestion;
import com.ican.elearning.dataobject.Question;
import com.ican.elearning.dataobject.User;
import com.ican.elearning.dto.ExamDTO;
import com.ican.elearning.dto.QuestionDTO;
import com.ican.elearning.service.ExamService;
import com.ican.elearning.service.QuestionService;
import com.ican.elearning.service.UserService;
import com.ican.elearning.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private ExamDao examDao;

    @Autowired
    private ExamQuestionDao examQuestionDao;

    @Autowired
    private UserService userService;

    private ExamDTO setExamDTO(ExamDTO examDTO,String examGrade){
        Integer examLevel = 0;
        List<QuestionDTO> questionDTOList = examDTO.getQuestionDTOList();
        for (QuestionDTO questionDTO :questionDTOList) {
            examLevel += questionDTO.getQuestionLevel();
        }
        examLevel /= questionDTOList.size();
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

        return examDTO;
    }

    @Override
    public  List<Exam> findByExamGradeAndExamNumber(String examGrade, Integer examNumber){
        return examDao.findByExamGradeAndExamNumber(examGrade,examNumber);
    }

    @Override
    public ExamDTO generateExam(String examGrade, Integer examNumber) {

        ExamDTO examDTO = new ExamDTO();
        List<Question> questionList = questionService.findByQuestionGrade(examGrade);
        if (questionList.size() < examNumber) {
            examDTO = examService.autoGenerateExam(examGrade, examNumber);
        }else {
            HashSet<Integer> set = new HashSet<Integer>();
            CommonUtil.randomSet(0,questionList.size(),examNumber,set);
            List<QuestionDTO> questionDTOList = new ArrayList<>();
            for(int selectNumber:set){
                questionDTOList.add(questionService.findByQuestionId(questionList.get(selectNumber).getQuestionId()));
            }
            questionDTOList = CommonUtil.questionSort(questionDTOList);
            examDTO.setQuestionDTOList(questionDTOList);
            examDTO = setExamDTO(examDTO,examGrade);
        }
        return examDTO;
    }

    @Override
    public ExamDTO autoGenerateExam(String examGrade, Integer examNumber) {

        ExamDTO examDTO = new ExamDTO();
        try {
            List<QuestionDTO> questionDTOList = questionService.autoGenerateQuestion(examGrade, examNumber);
            //TODO 試卷難度和試卷產生者和試卷標題
            examDTO.setQuestionDTOList(questionDTOList);
            examDTO = setExamDTO(examDTO,examGrade);
            for(QuestionDTO questionDTO:questionDTOList){
                questionService.insert(questionDTO);
            }
        } catch (Exception e) {
            log.error("【自動產生考卷】自動產生考題錯誤，error={}", e);
        }
        return examDTO;
    }

    @Override
    public ExamDTO generalExam(String examGrade, Integer examNumber) {
        List<Exam> examList = new ArrayList<>();
        examList = examService.findByExamGradeAndExamNumber(examGrade,examNumber);
        ExamDTO examDTO = new ExamDTO();
        if (examList.size() == 0) {
            examDTO = examService.generateExam(examGrade,examNumber);
        }else {
            int selectNumber = (int)(Math.random()*(examList.size()));
            Exam exam = examList.get(selectNumber);
            examDTO = examService.exam2ExamDTO(exam);
        }
        return examDTO;
    }

    @Override
    public ExamDTO dynamicExam(User user, Integer lastLevel, String result) {
        ExamDTO examDTO = new ExamDTO();
        QuestionDTO questionDTO = new QuestionDTO();
        Integer userLevel = user.getUserLevel();
        Integer nowLevel = 0;
        if (result.equals("True")) {
            nowLevel = lastLevel + 100;
            user.setUserLevel(lastLevel);
            user = userService.save(user);
            List<Question> questionList = questionService.findByQuestionLevelBetweenOrderByQuestionLevelDesc(userLevel,nowLevel);
            questionDTO = questionService.question2QuestionDTO(questionList.get(0));
        }else {
            nowLevel = lastLevel - 50;
            if (nowLevel.equals(user.getUserLevel())) {
                return null;

            } else {
                List<Question> questionList = questionService.findByQuestionLevelBetweenOrderByQuestionLevelDesc(userLevel,nowLevel);
                questionDTO = questionService.question2QuestionDTO(questionList.get(0));
            }
        }
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        questionDTOList.add(questionDTO);
        examDTO.setExamLevel(nowLevel);
        examDTO.setQuestionDTOList(questionDTOList);
        return examDTO;
    }

    @Override
    public Exam save(Exam exam) {
        return examService.save(exam);
    }

    @Override
    public ExamDTO exam2ExamDTO(Exam exam) {
        ExamDTO examDTO = new ExamDTO();
        List<ExamQuestion> examQuestionList = examQuestionDao.findByExamId(exam.getExamId());
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (ExamQuestion examQuestion: examQuestionList){
            Integer questionId = examQuestion.getQuestionId();
            QuestionDTO questionDTO = questionService.findByQuestionId(questionId);
            questionDTOList.add(questionDTO);
        }
        BeanUtils.copyProperties(exam,examDTO);
        examDTO.setQuestionDTOList(questionDTOList);
        return examDTO;
    }

        @Override
    public ExamDTO findExam(Integer examLevel) {
        ExamDTO examDTO = new ExamDTO();
        List<QuestionDTO> questionList = questionService.findByQuestionLevelBetween(examLevel-100,examLevel+100);

//        try {
//            List<QuestionDTO> questionDTOList = questionService.autoGenerateQuestion(examGrade, examNumber);
//            //TODO 試卷難度和試卷產生者和試卷標題
//            Integer examLevel = 0;
//            for (QuestionDTO questionDTO :questionDTOList) {
//                examLevel += questionDTO.getQuestionLevel();
//            }
//            examLevel /= questionDTOList.size();
////          examDTO.setExamLevel(Integer.valueOf(examGrade) * 100);
//            examDTO.setExamLevel(examLevel);
//            examDTO.setCreateBy("AutoGenerate");
//            examDTO.setExamTitle("AutoGenerateExam");
//
//            String examHashtag = "";
//            for(QuestionDTO questionDTO:questionDTOList) {
//                examHashtag = examHashtag + questionDTO.getQuestionHashtag() + ";";
//            }
//
//            examDTO.setExamHashtag(examHashtag);
//            examDTO.setExamNumber(questionDTOList.size());
//            examDTO.setExamGrade(examGrade);
//            examDTO.setQuestionDTOList(questionDTOList);
//
//        } catch (Exception e) {
//            log.error("【自動產生考卷】自動產生考題錯誤，error={}", e);
//        }

        return examDTO;
    }
}

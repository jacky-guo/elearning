package com.ican.elearning.service.impl;

import com.google.gson.Gson;
import com.ican.elearning.Dao.QuestionDao;
import com.ican.elearning.dataobject.*;
import com.ican.elearning.dto.AnswerDTO;
import com.ican.elearning.dto.ExamDTO;
import com.ican.elearning.dto.QuestionDTO;
import com.ican.elearning.service.*;
import com.ican.elearning.utils.AutoGenerator;
import com.ican.elearning.utils.Lemmatizer;
import lombok.extern.slf4j.Slf4j;
import org.python.antlr.op.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by JackyGuo
 * 2017/9/4 17:49
 */
@Slf4j
@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private ParagraphService paragraphService;
    @Autowired
    private WordService wordService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private UserService userService;

    @Override
    public List<Question> findAll() {
        return questionDao.findAll();
    }

    @Override
    public QuestionDTO findByQuestionId(Integer questionId) {
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = questionDao.findOne(questionId);
        BeanUtils.copyProperties(question,questionDTO);
        List<AnswerDTO> answerDTOList = answerService.findByQuestionId(questionId);
        questionDTO.setAnswerDTOList(answerDTOList);
        return questionDTO;
    }

    @Override
    public void delete(Integer questionId) {
        answerService.delete(questionId);
        questionDao.delete(questionId);
    }

    @Override
    public Question save(Question question) {
        return questionDao.save(question);
    }

    @Override
    public Question findOne(Integer questionId) {
        return questionDao.findOne(questionId);
    }

    @Override
    public Page<Question> findByQuestionGrade(String questionGrade, Pageable pageable) {
        return questionDao.findByQuestionGrade(questionGrade,pageable);
    }

    @Override
    public List<Question> findByQuestionGrade(String questionGrade) {
        return questionDao.findByQuestionGrade(questionGrade);
    }

    @Override
    public QuestionDTO insert(QuestionDTO questionDTO){
        Question question = new Question();
        BeanUtils.copyProperties(questionDTO,question);
        List<AnswerDTO> answerDTOList = questionDTO.getAnswerDTOList();
        question = questionDao.save(question);
        Gson gson = new Gson();
        for(int i=0;i<answerDTOList.size();i++) {
            Answer answer = new Answer();
            answer.setQuestionId(question.getQuestionId());
            answer.setAnswerOrders(answerDTOList.get(i).getAnswerOrders());
            answer.setCreateBy(question.getCreateBy());
            answer.setAnswerContent(gson.toJson(answerDTOList.get(i)));
            Answer ansResult = answerService.save(answer);
        }
        return questionDTO;
    }

    @Override
    public List<QuestionDTO> findByQuestionLevelBetween(Integer levelLower, Integer levelUpper) {
        List<Question> questionList = questionDao.findByQuestionLevelBetween(levelLower,levelUpper);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question:questionList) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            question.getQuestionId();
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }

    public QuestionDTO autoGenerateQuestionbyWord(){
        QuestionDTO questionDTO = new QuestionDTO();
        return questionDTO;
    }

    public QuestionDTO autoGenerateQuestionbyGrade(String grade){
        QuestionDTO questionDTO = new QuestionDTO();

        List<Paragraph> paragraphList = paragraphService.findByParagraphGradeOrderByCreateTimeDesc(grade);
        paragraphList = paragraphList.subList(0,100);

        return questionDTO;
    }

    @Override
    public List<QuestionDTO> autoGenerateQuestion(String questionGrade, Integer questionNumber) {
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        List<Paragraph> paragraphList = paragraphService.findByParagraphGradeOrderByCreateTimeDesc(questionGrade);
        List<Word> wordList = wordService.findByWordGrade(questionGrade);
        questionDTOList = Lemmatizer.autoGenerator(paragraphList,wordList,questionNumber);
        return questionDTOList;
    }

    @Override
    public List<Question> findByQuestionLevelBetweenOrderByQuestionLevelDesc(Integer levelLower, Integer levelUpper) {
        return questionDao.findByQuestionLevelBetweenOrderByQuestionLevelDesc(levelLower,levelUpper);
    }

    @Override
    public QuestionDTO question2QuestionDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        List<AnswerDTO> answerDTOList = answerService.findByQuestionId(question.getQuestionId());
        questionDTO.setAnswerDTOList(answerDTOList);
        return questionDTO;
    }

    public QuestionDTO interactiveQuestion(User user, Integer lastLevel, String result) {
        QuestionDTO questionDTO = new QuestionDTO();
        Integer userLevel = user.getUserLevel();
        if (result.equals("True")) {
            Integer nowLevel = lastLevel + 100;
            user.setUserLevel(lastLevel);
            user = userService.save(user);
            List<Question> questionList = questionDao.findByQuestionLevelBetweenOrderByQuestionLevelDesc(userLevel,nowLevel);
            questionDTO = question2QuestionDTO(questionList.get(0));
        }else {
            Integer nowLevel = lastLevel - 50;
            if (nowLevel.equals(user.getUserLevel())) {
                return null;

            } else {
                List<Question> questionList = questionDao.findByQuestionLevelBetweenOrderByQuestionLevelDesc(userLevel,nowLevel);
                questionDTO = question2QuestionDTO(questionList.get(0));
            }
        }
        return questionDTO;
    }

//    @Override
    public List<QuestionDTO> autoGenerateQuestion1(String questionGrade, Integer questionNumber) {

        //取得當前時間和5天前的日期 確認教材抓取時間範圍
        int dayBefore = 0;
        int paragraphListsize = 0;
        Date dateNow = new Date();
        Calendar dateBefore =Calendar.getInstance();
        dateBefore.setTime(dateNow);

        //
        List<String> questionGradeList = new ArrayList<>();
        questionGradeList.add(questionGrade);
        List<Paragraph> paragraphList = new ArrayList<>();
        while (paragraphListsize < 3 ) {
            dateBefore.set(Calendar.DATE,dateBefore.get(Calendar.DATE)-(dayBefore+=5));
            paragraphList = paragraphService.findByCreateTimeBetween(dateBefore.getTime(),dateNow);
            try {
                paragraphListsize = paragraphList.size();
            } catch (Exception e) {
                log.error("【自動產生考題】近{}天內沒有新教材，error={}",dayBefore,e);
            }
            if(dayBefore > 100){
                break;
            }
        }
        //
        List<Word> wordList = wordService.findByWordGradeIn(questionGradeList);
        List<Paragraph> findParagraphList = AutoGenerator.searchFitnessParagraph(paragraphList,wordList,questionNumber);


        Map<String,String> dbWordMap = new LinkedHashMap<>();
        for (Word word:wordList) {
            dbWordMap.put(word.getWordContent(),word.getWordPartofspeech());
        }

        List<QuestionDTO> result = new ArrayList<>();
        for(Paragraph paragraph:findParagraphList) {
//            Map<String, String> wordMap = Lemmatizer.lemma(paragraph.getParagraphContent());
//            Set<String> wordSet = wordMap.keySet();
//            wordSet.retainAll(dbWordSet);
            QuestionDTO questionDTO = Lemmatizer.someReplace(paragraph,dbWordMap);
            if (questionDTO == null ) {
                //TODO null處理
            }else {
                result.add(questionDTO);
            }
        }
        return result;
    }
}

package com.ican.elearning.service.impl;

import com.ican.elearning.Dao.QuestionDao;
import com.ican.elearning.dataobject.Paragraph;
import com.ican.elearning.dataobject.Question;
import com.ican.elearning.dataobject.Word;
import com.ican.elearning.dto.QuestionDTO;
import com.ican.elearning.service.ParagraphService;
import com.ican.elearning.service.QuestionService;
import com.ican.elearning.service.WordService;
import com.ican.elearning.utils.AutoGenerator;
import com.ican.elearning.utils.Lemmatizer;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public List<Question> findAll() {
        return questionDao.findAll();
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
    public List<QuestionDTO> autoGenerateQuestion(String questionGrade, Integer questionNumber) {

        //取得當前時間和5天前的日期 確認教材抓取時間範圍
        int dayBefore = 5;
        int paragraphListsize = 0;
        Date dateNow = new Date();
        Calendar dateBefore =Calendar.getInstance();
        dateBefore.setTime(dateNow);
        dateBefore.set(Calendar.DATE,dateBefore.get(Calendar.DATE)-dayBefore);

        List<String> questionGradeList = new ArrayList<>();
        questionGradeList.add(questionGrade);
        List<Paragraph> paragraphList = new ArrayList<>();
        while (paragraphListsize < 3 ) {
            paragraphList = paragraphService.findByCreateTimeBetween(dateBefore.getTime(),dateNow);
            try {
                paragraphListsize = paragraphList.size();
            } catch (Exception e) {
                log.error("【自動產生考題】近期{}天內沒有新教材，error={}",dayBefore,e);
            }
            dateBefore.set(Calendar.DATE,dateBefore.get(Calendar.DATE)-(dayBefore+=5));
        }
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

    @Override
    public Page<Question> findByQuestionGrade(String questionGrade, Pageable pageable) {
        return questionDao.findByQuestionGrade(questionGrade,pageable);
    }
}

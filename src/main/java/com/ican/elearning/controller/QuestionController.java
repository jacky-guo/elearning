package com.ican.elearning.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ican.elearning.VO.ResultVO;
import com.ican.elearning.dataobject.Answer;
import com.ican.elearning.dataobject.Question;
import com.ican.elearning.dataobject.Word;
import com.ican.elearning.dto.AnswerDTO;
import com.ican.elearning.dto.QuestionDTO;
import com.ican.elearning.enums.ResultEnum;
import com.ican.elearning.exception.ElearningException;
import com.ican.elearning.form.ParagraphForm;
import com.ican.elearning.form.QuestionForm;
import com.ican.elearning.service.AnswerService;
import com.ican.elearning.service.ParagraphService;
import com.ican.elearning.service.QuestionService;
import com.ican.elearning.service.WordService;
import com.ican.elearning.utils.Lemmatizer;
import com.ican.elearning.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * Created by JackyGuo
 * 2017/9/5 0:14
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private WordService wordService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private ParagraphService paragraphService;

    //新增題目
    @PostMapping("insert")
    public ResultVO<Question> insert(@Valid QuestionForm questionForm,
                                     BindingResult bindingResult) {

        Question question = new Question();
        question.setCreateBy(questionForm.getCreateBy());
        question.setQuestionContent(questionForm.getQuestionContent());
        question.setQuestionHashtag(questionForm.getQuestionHashtag());
        question.setQuestionLevel(Integer.valueOf(questionForm.getQuestionGrade())*100);
        question.setQuestionGrade(questionForm.getQuestionGrade());
        question.setQuestionParagraphId(questionForm.getQuestionParagraphId());
        question.setQuestionType(questionForm.getQuestionType());

        Question questionResult = questionService.save(question);

        Gson gson = new Gson();
        List<AnswerDTO> answerDTOListList = new ArrayList<>();
        try {
            answerDTOListList = gson.fromJson(questionForm.getAns(),
                    new TypeToken<List<AnswerDTO>>() {
                    }.getType());
        } catch (Exception e) {
            log.error("【對象轉換】錯誤，string={}",questionForm.getAns());
        }

        for(int i=0;i<answerDTOListList.size();i++) {
            Answer answer = new Answer();
            answer.setQuestionId(questionResult.getQuestionId());
            answer.setAnswerOrders(i);
            answer.setCreateBy(questionResult.getCreateBy());
            answer.setAnswerContent(gson.toJson(answerDTOListList.get(i)));
            System.out.println(answer.getAnswerContent());
            Answer ansResult = answerService.save(answer);
        }

        return ResultVOUtil.success();
    }

    //自動產生題目
    @PostMapping("/autoGenerated")
    public ResultVO<Question> autoGenerated(@Valid ParagraphForm paragraphForm,
                                            BindingResult bindingResult) {

        //將句子斷詞后取得詞性、詞根
        List<Map<String,String>> wordList = Lemmatizer.sentencesParser(paragraphForm.getParagraphContent());
        List<String> contentList = new ArrayList<>();
        List<Map<String,Object>> resultList = new ArrayList<>();

        Map<String,Word> wordMap = new LinkedHashMap<>();

        //取得句子中所有的詞性
        HashSet<String> posSet = new HashSet<>();
        for(Map<String,String> word:wordList) {
            contentList.add(word.get("lemma"));
            String pos = word.get("pos");
            posSet.add(pos);
            List<Word> selfList = wordService.findByWordContentAndWordPartofspeech(word.get("lemma"),pos);
            if (selfList.size()>0) {
                wordMap.put(word.get("lemma"), selfList.get(0));
            }else {
                wordMap.put(word.get("lemma"), null);
            }
        }

        //key:詞性,value:資料庫中該詞性的所有詞
        Map<String,List<Word>> samePosWordMap = new LinkedHashMap<>();
        for(String key:posSet) {
            List<Word> samePosWordList = wordService.findByWordPartofspeech(key);
            samePosWordMap.put(key,samePosWordList);
        }

        //找可替換的選項
        for(Map<String,String> word:wordList) {
            List<Word> samePosWordList = samePosWordMap.get(word.get("pos"));
            samePosWordList.remove(wordMap.get(word.get("lemma")));
            List<String> falseAnsList = new ArrayList<>();
            if(samePosWordList.size()>=4) {
                for(int i =0;i<3;i++) {
                    int j =(int)(Math.random()*samePosWordList.size());
                    falseAnsList.add(samePosWordList.get(j).getWordContent());
                    samePosWordList.remove(j);
                }
            }else {

            }
            Map<String,Object> ansMap = new HashMap<>();
            ansMap.put("content",word.get("content"));
            ansMap.put("falseAnsList",falseAnsList);
            resultList.add(ansMap);
        }

//        List<Word> dbList = wordService.findByWordContentInAndWordGrade(contentList,paragraphForm.getParagraphGrade());
//        List<String> dbWordList = new ArrayList<>();
//        for(Word word:dbList) {
//            dbWordList.add(word.getWordContent());
//        }

        HashMap hashMap = new HashMap();
        hashMap.put("grade",paragraphForm.getParagraphGrade());
        hashMap.put("type",1);
        hashMap.put("hashtag",paragraphForm.getParagraphHashtag());
        hashMap.put("data",resultList);

        return ResultVOUtil.success(hashMap);
    }

    @GetMapping("/list")
    public ResultVO<List<Question>> list(@RequestParam("grade") String questionGrade,
                                     @RequestParam(value = "page",defaultValue = "0") Integer page,
                                     @RequestParam(value = "size",defaultValue = "20") Integer size) {
        if (StringUtils.isEmpty(questionGrade)) {
            log.error("【查詢題目列表】grade為空");
            throw new ElearningException(ResultEnum.PARAM_ERROR);
//            return ResultVOUtil.error(400,"請選擇年級");
        }

        PageRequest request = new PageRequest(page,size);
        Page<Question> questionPage = questionService.findByQuestionGrade(questionGrade,request);
        HashMap hashMap = new HashMap();
        hashMap.put("count",questionPage.getTotalElements());
        hashMap.put("data",questionPage.getContent());

        return ResultVOUtil.success(hashMap);
    }

    @GetMapping("/findOne")
    public ResultVO<QuestionDTO> findOne(@RequestParam("questionId") Integer questionId) {
        Question question = questionService.findOne(questionId);
        List<Answer> answerList = answerService.findByQuestionId(questionId);
        Gson gson = new Gson();
        List<AnswerDTO> answerDTOList = new ArrayList<>();
        for (Answer answer:answerList) {
            AnswerDTO answerDTO = new AnswerDTO();
            try {
                answerDTO = gson.fromJson(answer.getAnswerContent(),
                        new TypeToken<AnswerDTO>() {
                        }.getType());
            } catch (Exception e) {
                log.error("【對象轉換】錯誤，string={}",answer.getAnswerContent());
            }
            answerDTOList.add(answerDTO);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setAnswerDTOList(answerDTOList);
        return ResultVOUtil.success(questionDTO);
    }


}

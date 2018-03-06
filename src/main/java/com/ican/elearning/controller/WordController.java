package com.ican.elearning.controller;

import com.ican.elearning.VO.ResultVO;
import com.ican.elearning.dataobject.Word;
import com.ican.elearning.enums.ResultEnum;
import com.ican.elearning.exception.ElearningException;
import com.ican.elearning.form.WordForm;
import com.ican.elearning.service.WordService;
import com.ican.elearning.utils.RestTemplate;
import com.ican.elearning.utils.ResultVOUtil;
import com.sun.org.apache.regexp.internal.RE;
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
 * 2017/9/1 16:47
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/word")
public class WordController {
    @Autowired
    private WordService wordService;

    //新增單字
    @PostMapping("/insert")
    public ResultVO<Word> insert(@Valid WordForm wordForm,
                                 BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                log.error("【新增單字】參數不正確，wordForm={}", wordForm);
                throw new ElearningException(ResultEnum.PARAM_EMPTY_ERROR.getCode(),
                        bindingResult.getFieldError().getDefaultMessage());
            }
        } catch (ElearningException e) {
            //如果有異常拋出錯誤訊息
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }

        Word word = new Word();
        BeanUtils.copyProperties(wordForm,word);
        Integer word_length = word.getWordContent().length();
        if (word.getWordLevel() == null) {
            try {
                word.setWordLevel(RestTemplate.getLevel(word));
            } catch (Exception e) {
                return ResultVOUtil.error(ResultEnum.PYTHONSERVER_ERROR.getCode(),ResultEnum.PYTHONSERVER_ERROR.getMessage());
            }
        }

        if (word.getWordGrade() == null) {
            word.setWordGrade(Integer.toString(word.getWordLevel()/100));
        }

        word.setWordLength(word_length);
        Word insertResult = wordService.save(word);
        return ResultVOUtil.success(insertResult.getWordId());
    }

    //相似搜尋
    @GetMapping(value = "query")
    public ResultVO<List<Word>> query(@RequestParam(value= "wordContent",defaultValue = "") String wordContent,
                                      @RequestParam(value = "page",defaultValue = "0") Integer page,
                                      @RequestParam(value = "size",defaultValue = "20") Integer size) {
        try {
            if (StringUtils.isEmpty(wordContent)) {
                log.error("【單字查詢(相似搜尋)】wordContent為空");
                throw new ElearningException(ResultEnum.PARAM_EMPTY_ERROR);
            }
        } catch (ElearningException e) {
            //如果有異常拋出錯誤訊息
            return ResultVOUtil.error(e.getCode(),e.getMessage()+"，wordContent不能為空");
        }

        PageRequest request = new PageRequest(page,size);
        Page<Word> wordPage = wordService.findByWordContentStartingWith(wordContent,request);
        HashMap hashMap = new HashMap();
        hashMap.put("count",wordPage.getTotalElements());
        hashMap.put("wordList",wordPage.getContent());

        return ResultVOUtil.success(hashMap);

    }

    //查詢單字列表
    @GetMapping(value="/list")
    public ResultVO<List<Word>> list(@RequestParam(value= "grade",defaultValue = "3") String wordGrade,
                                     @RequestParam(value= "level",required = false) Integer wordLevel,
                                     @RequestParam(value = "range",defaultValue = "5") Integer range,
                                     @RequestParam(value = "page",defaultValue = "0") Integer page,
                                     @RequestParam(value = "size",defaultValue = "20") Integer size) {
        try {
            if (StringUtils.isEmpty(wordGrade) && wordLevel == null) {
                log.error("【查詢單字列表】grade和level為空");
                throw new ElearningException(ResultEnum.PARAM_EMPTY_ERROR);
            }
        } catch (ElearningException e) {
            //如果有異常拋出錯誤訊息
            return ResultVOUtil.error(e.getCode(),e.getMessage()+"，grade和level不能同時為空");
        }

        PageRequest request = new PageRequest(page,size);
        Page<Word> wordPage;
        if (wordLevel != null) {
            wordPage = wordService.findByWordLevelBetween(wordLevel - range, wordLevel + range, request);
        }else {
            wordPage = wordService.findByWordGrade(wordGrade,request);
        }

//        List<WordVO> wordVOList = new ArrayList<>();
//        for(Word word : wordPage.getContent()) {
//            WordVO wordVO = new WordVO();
//            BeanUtils.copyProperties(word,wordVO);
//            wordVOList.add(wordVO);
//        }
        HashMap hashMap = new HashMap();
        hashMap.put("count",wordPage.getTotalElements());
        hashMap.put("wordList",wordPage.getContent());

        return ResultVOUtil.success(hashMap);
    }

    //單字刪除
    @DeleteMapping("/delete")
    public ResultVO<Word> delete(@RequestParam("wordId") Integer wordId) {
        try {
            wordService.delete(wordId);
        } catch (Exception e) {
            //如果有異常拋出錯誤訊息
            log.error("【刪除單字】刪除內容不存在,wordId={},錯誤訊息={}",wordId,e.getMessage());
            return ResultVOUtil.error(ResultEnum.NOT_EXIST_ERROR.getCode(),ResultEnum.NOT_EXIST_ERROR.getMessage());
        }
        return ResultVOUtil.success();
    }

    @GetMapping("/pie")
    public ResultVO<Map<String,Integer>> pie(){

        HashMap map = new HashMap();
        map.put("grade3",wordService.findByWordGradeIn(Arrays.asList("3")).size());
        map.put("grade4",wordService.findByWordGradeIn(Arrays.asList("4")).size());
        map.put("grade5",wordService.findByWordGradeIn(Arrays.asList("5")).size());
        map.put("grade6",wordService.findByWordGradeIn(Arrays.asList("6")).size());
        return ResultVOUtil.success(map);
    }
}

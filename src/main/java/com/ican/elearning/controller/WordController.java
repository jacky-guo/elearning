package com.ican.elearning.controller;

import com.ican.elearning.VO.ResultVO;
import com.ican.elearning.dataobject.Word;
import com.ican.elearning.enums.ResultEnum;
import com.ican.elearning.exception.ElearningException;
import com.ican.elearning.form.WordForm;
import com.ican.elearning.service.WordService;
import com.ican.elearning.utils.RestTemplate;
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
 * 2017/9/1 16:47
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/word")
public class WordController {
    @Autowired
    private WordService wordService;

    @PostMapping("/insert")
    public ResultVO<Word> insert(@Valid WordForm wordForm,
                                 BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                log.error("【新增單字】參數不正確，wordForm={}", wordForm);
                throw new ElearningException(ResultEnum.PARAM_ERROR.getCode(),
                        bindingResult.getFieldError().getDefaultMessage());
            }
        } catch (Exception e) {
            //如果有異常拋出錯誤訊息
            return ResultVOUtil.error(ResultEnum.BADREQUEST.getCode(),e.getMessage());
        }
        Word word = new Word();
        BeanUtils.copyProperties(wordForm,word);
        Integer word_length = word.getWordContent().length();
        if (word.getWordLevel() == null) {
            word.setWordLevel(RestTemplate.getLevel(word));
//            if (word.getWordGrade() == null) {
//                Integer word_level = Integer.valueOf(wordForm.getWordGrade()) * 100;
//                word.setWordLevel(word_level);
//            }
        }

        if (word.getWordGrade() == null) {
            word.setWordGrade(Integer.toString(word.getWordLevel()/100));
        }

        word.setWordLength(word_length);
        Word insertResult = wordService.save(word);
        return ResultVOUtil.success(insertResult.getWordId());
    }

    @GetMapping(value = "query")
    public ResultVO<List<Word>> query(@RequestParam(value= "wordContent") String wordContent,
                                      @RequestParam(value = "page",defaultValue = "0") Integer page,
                                      @RequestParam(value = "size",defaultValue = "20") Integer size) {
        try {
            if (StringUtils.isEmpty(wordContent)) {
                log.error("【單字查詢(相似搜尋)】wordContent為空");
                throw new ElearningException(ResultEnum.PARAM_ERROR);
            }
        } catch (Exception e) {
            //如果有異常拋出錯誤訊息
            return ResultVOUtil.error(ResultEnum.BADREQUEST.getCode(),e.getMessage());
        }

        PageRequest request = new PageRequest(page,size);
        Page<Word> wordPage = wordService.findByWordContentStartingWith(wordContent,request);
        HashMap hashMap = new HashMap();
        hashMap.put("count",wordPage.getTotalElements());
        hashMap.put("wordList",wordPage.getContent());

        return ResultVOUtil.success(hashMap);

    }

    @GetMapping(value="/list")
    public ResultVO<List<Word>> list(@RequestParam(value= "grade",required = false) String wordGrade,
                                     @RequestParam(value= "level",required = false) Integer wordLevel,
                                     @RequestParam(value = "range",defaultValue = "5") Integer range,
                                     @RequestParam(value = "page",defaultValue = "0") Integer page,
                                     @RequestParam(value = "size",defaultValue = "20") Integer size) {
        try {
            if (StringUtils.isEmpty(wordGrade) && wordLevel == null) {
                log.error("【查詢單字列表】grade和level為空");
                throw new ElearningException(ResultEnum.PARAM_ERROR);
            }
        } catch (Exception e) {
            //如果有異常拋出錯誤訊息
            return ResultVOUtil.error(ResultEnum.BADREQUEST.getCode(),e.getMessage());
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

    @DeleteMapping("/delete")
    public ResultVO<Word> delete(@RequestParam("wordId") Integer wordId) {
        try {
            wordService.delete(wordId);

        } catch (Exception e) {
            //如果有異常拋出錯誤訊息
            log.error("【刪除單字】刪除內容不存在,wordId={},錯誤訊息={}",wordId,e.getMessage());
            return ResultVOUtil.error(ResultEnum.FORBIDDEN.getCode(),ResultEnum.DELETE_ERROR.getMessage());
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

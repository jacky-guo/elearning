package com.ican.elearning.controller;

import com.ican.elearning.VO.ResultVO;
import com.ican.elearning.converter.ParagraphForm2ParagraphConverter;
import com.ican.elearning.dataobject.Paragraph;
import com.ican.elearning.dataobject.Word;
import com.ican.elearning.enums.ResultEnum;
import com.ican.elearning.exception.ElearningException;
import com.ican.elearning.form.ParagraphForm;
import com.ican.elearning.service.ParagraphService;
import com.ican.elearning.service.WordService;
import com.ican.elearning.utils.Lemmatizer;
import com.ican.elearning.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
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
 * 2017/9/4 15:28
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/paragraph")
public class ParagraphController {

    @Autowired
    private ParagraphService paragraphService;

    @Autowired
    private WordService wordService;

    //新增教材
    @PostMapping("/insert")
    public ResultVO<Paragraph> insert(@Valid ParagraphForm paragraphForm,
                                      BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                log.error("【新增教材】參數不正確，paragraphForm={}", paragraphForm);
                throw new ElearningException(ResultEnum.PARAM_EMPTY_ERROR.getCode(),
                        bindingResult.getFieldError().getDefaultMessage());
            }
        } catch (ElearningException e) {
            //如果有異常拋出錯誤訊息
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }

        Paragraph paragraph = ParagraphForm2ParagraphConverter.convert(paragraphForm);
        Paragraph insertResult = paragraphService.save(paragraph);

        //將句子切割 回傳原型和詞性
        Map<String,String> contentMap = Lemmatizer.lemma(insertResult.getParagraphContent());

        //查找已經存在DB的單字
        List<String> contentList = new ArrayList<>(contentMap.keySet());
        List<Word> dbWordList = wordService.findByWordContentIn(contentList);

        //將已經存在DB的單字從contentMap中去除
        List<String> listOfKeysToRemove = new ArrayList<>();
        for (Word a:dbWordList) {
            listOfKeysToRemove.add(a.getWordContent());
        }
        contentMap.keySet().removeAll(listOfKeysToRemove);

        List<Word> wordList = new ArrayList<>();

        for(Map.Entry<String,String> entry :contentMap.entrySet()) {
            Word word = new Word();
            word.setWordContent(entry.getKey());
            word.setWordPartofspeech(entry.getValue());
            word.setWordGrade(paragraph.getParagraphGrade());
            word.setWordSource(paragraph.getParagraphSource());
            word.setCreateBy(paragraph.getCreateBy());
            wordList.add(word);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("ParagraphId",insertResult.getParagraphId());
        hashMap.put("data",wordList);

        return ResultVOUtil.success(hashMap);
    }

    //查詢教材列表
    @GetMapping("/list")
    public ResultVO<List<Paragraph>> list(@RequestParam(value = "grade", defaultValue = "3") String paragraphGrade,
                                          @RequestParam(value = "level", required = false) Integer paragraphLevel,
                                          @RequestParam(value = "range", defaultValue = "5") Integer range,
                                          @RequestParam(value = "page", defaultValue = "0") Integer page,
                                          @RequestParam(value = "size", defaultValue = "20") Integer size) {
        try {
            if (StringUtils.isEmpty(paragraphGrade) && paragraphLevel == null) {
                log.error("【查詢教材列表】grade和level為空");
                throw new ElearningException(ResultEnum.PARAM_EMPTY_ERROR);
            }
        } catch (ElearningException e) {
            return ResultVOUtil.error(e.getCode(),e.getMessage());
        }

        PageRequest request = new PageRequest(page,size);
        Page<Paragraph> paragraphPage;
        if (paragraphLevel != null ) {
            paragraphPage = paragraphService.findByParagraphLevelBetween(paragraphLevel - range, paragraphLevel + range ,request);
        }else {
            paragraphPage = paragraphService.findByParagraphGrade(paragraphGrade,request);
        }

        HashMap hashMap = new HashMap();
        hashMap.put("count",paragraphPage.getTotalElements());
        hashMap.put("paragraphList",paragraphPage.getContent());

        return ResultVOUtil.success(hashMap);
    }

    //精確查詢(按教材ID查詢)
    @GetMapping("/query")
    public ResultVO<Paragraph> query(@RequestParam("paragraphId") Integer paragraphId) {
        Paragraph paragraph;
        try {
            paragraph = paragraphService.findByParagraphId(paragraphId);
        } catch (Exception e) {
            //如果有異常拋出錯誤訊息
            log.error("【教材ID查詢】查詢內容不存在,paragraphId={},錯誤訊息={}",paragraphId,e.getMessage());
            return ResultVOUtil.error(ResultEnum.NOT_EXIST_ERROR.getCode(),ResultEnum.NOT_EXIST_ERROR.getMessage());
        }
        return ResultVOUtil.success(paragraph);
    }

    //刪除教材
    @DeleteMapping("/delete")
    public ResultVO<Paragraph> delete(@RequestParam("paragraphId") Integer paragraphId) {
        try {
            paragraphService.delete(paragraphId);

        } catch (Exception e) {
            //如果有異常拋出錯誤訊息
            log.error("【刪除教材】刪除內容不存在,paragraphId={},錯誤訊息={}",paragraphId,e.getMessage());
            return ResultVOUtil.error(ResultEnum.NOT_EXIST_ERROR.getCode(),ResultEnum.NOT_EXIST_ERROR.getMessage());
        }
        return ResultVOUtil.success();
    }
}

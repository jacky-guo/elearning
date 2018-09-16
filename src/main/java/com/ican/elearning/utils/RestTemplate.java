package com.ican.elearning.utils;

import com.ican.elearning.dataobject.Paragraph;
import com.ican.elearning.dataobject.Word;
import org.springframework.http.*;

public class RestTemplate {
    public static Integer getLevel(Word word) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Word> entity = new HttpEntity<Word>(word, headers);
        System.out.println(entity.getHeaders());
        ResponseEntity<Word> respEntity = new org.springframework.web.client.RestTemplate().exchange(
                "http://localhost:5000/word",
                HttpMethod.POST,
                entity,
                Word.class);

        Word respVo = respEntity.getBody();
        Integer wordLevel = respVo.getWordLevel();
        System.out.println(respVo.getWordLevel());
        return wordLevel;
    }

    public static Paragraph getParagraphLevel(Paragraph paragraph) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Paragraph> entity = new HttpEntity<Paragraph>(paragraph, headers);
        System.out.println(entity.getHeaders());
        ResponseEntity<Paragraph> respEntity = new org.springframework.web.client.RestTemplate().exchange(
                "http://localhost:5000/paragraph",
                HttpMethod.POST,
                entity,
                Paragraph.class);

        Paragraph respVo = respEntity.getBody();
        Integer paragraphLevel = respVo.getParagraphLevel();
        String paragraphGrade = respVo.getParagraphGrade();
        return respVo;
    }
}

package com.ican.elearning.utils;

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
                "http://localhost:5000/test",
                HttpMethod.POST,
                entity,
                Word.class);

        Word respVo = respEntity.getBody();
        Integer wordLevel = respVo.getWordLevel();
        System.out.println(respVo.getWordLevel());
        return wordLevel;
    }
}

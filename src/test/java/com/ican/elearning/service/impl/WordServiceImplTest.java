package com.ican.elearning.service.impl;

import com.ican.elearning.dataobject.Word;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by JackyGuo
 * 2017/9/1 16:03
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WordServiceImplTest {

    @Autowired
    private WordServiceImpl wordService;

    @Test
    public void save() throws Exception {

    }

    @Test
    public void findOne() throws Exception {
        Word word = wordService.findOne(1);
        Assert.assertEquals("banana",word.getWordContent());

    }

    @Test
    public void findAll() throws Exception {
    }

    @Test
    public void findByWordLevelIn() throws Exception {

        List<Word> wordList = wordService.findByWordLevelIn(Arrays.asList(400,500));
        Assert.assertNotEquals(0,wordList.size());
        System.out.println(wordList.size());

    }

    @Test
    public void findByWordContent() throws Exception {
        List<Word> word = wordService.findByWordContent("sadasdasdasdad");
        if(word.size()>0) {
            System.out.println(word.get(0));
        }else {
            System.out.println(word.get(0));
        }
    }

    @Test
    public void findByWordGradeIn() throws Exception {
    }

    @Test
    public void findByWordContentIn() throws Exception {
    }

}
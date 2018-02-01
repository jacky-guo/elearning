package com.ican.elearning.service.impl;

import com.ican.elearning.dataobject.Question;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by JackyGuo
 * 2017/9/18 1:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuestionServiceImplTest {
    @Autowired
    private QuestionServiceImpl questionService;


    @Test
    public void save() throws Exception {
        Question question = new Question();
        question.setQuestionLevel(400);
        question.setQuestionContent("123");
        question.setQuestionType(1);
        question.setQuestionId(1);
        Question result = questionService.save(question);
        Assert.assertNotNull(result);
    }

    @Test
    public void findOne() throws Exception {
        Question question = questionService.findOne(1);
        System.out.println(question);
        Assert.assertNotNull(question);

    }

}
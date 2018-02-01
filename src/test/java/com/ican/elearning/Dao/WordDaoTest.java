package com.ican.elearning.Dao;

import com.ican.elearning.dataobject.Word;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * Created by JackyGuo
 * 2017/9/1 14:43
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WordDaoTest {
    @Autowired
    private WordDao wordDao;

    @Test
    public void findOneTest() {
        Word word = wordDao.findOne(1);
        System.out.println(word.toString());
    }

    @Test
//    @Transactional //成功后刪除
    public void saveTest() {

        Word word = wordDao.findOne(1);
        word.setWordContent("banana");
        word.setWordLevel(400);
        word.setWordLength(5);
        Word result = wordDao.save(word);
        System.out.println(word.toString());
        Assert.assertNotNull(result);
    }

    public void updateTest() {
    }
}
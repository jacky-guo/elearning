package com.ican.elearning.Dao;

import com.ican.elearning.dataobject.Paragraph;
import com.ican.elearning.dataobject.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by JackyGuo
 * 2017/9/1 15:33
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ParagraphDao paragraphDao;

    @Test
    public void findOneTest() {
//        User user = userDao.findOne("1");
//        System.out.println(user.toString());
        try {
            String dateString = "30-09-2017 12:00";
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date date = sdf.parse(dateString);
            Timestamp timets = new Timestamp(date.getTime());
            System.out.println(timets);
            Timestamp time1 = new Timestamp(System.currentTimeMillis());
            System.out.println(time1);

            Date d = new Date();
            Date dBefore = new Date();
            System.out.println(d.getTime());
            Calendar now =Calendar.getInstance();
            now.setTime(d);
            now.set(Calendar.DATE,now.get(Calendar.DATE)-5);
            dBefore = now.getTime();
            System.out.println(dBefore);
            List<Paragraph> paragraphList = paragraphDao.findByCreateTimeBetween(now.getTime(),d);
            System.out.println(paragraphList.size());
        }catch (Exception e)  {

        }

    }
}
package com.ican.elearning.service.impl;

import com.ican.elearning.Dao.UserDao;
import com.ican.elearning.dataobject.User;
import com.ican.elearning.enums.ResultEnum;
import com.ican.elearning.exception.ElearningException;
import com.ican.elearning.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by JackyGuo
 * 2017/9/2 17:19
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User login(String userAccount, String userPassword) {
        return userDao.findByUserAccountAndUserPassword(userAccount,userPassword);
    }

    @Override
    public User register(User user) {
        User newUser = userDao.findByUserAccount(user.getUserAccount());
        if (newUser != null) {
            log.error("【用戶註冊】註冊失敗賬號已存在,user={}",user);
            throw new ElearningException(ResultEnum.ACCOUNT_EXIST);
        }
        return userDao.save(user);
    }

    @Override
    public User findByUserAccount(String userAccount) {
        return userDao.findByUserAccount(userAccount);
    }

    @Override
    public User findByUserAccountAndUserPassword(String userAccount,String userPassword) {
        return userDao.findByUserAccountAndUserPassword(userAccount,userPassword);
    }

    @Override
    public User save(User user) {
        user.setLastloginTime(null);
        return userDao.save(user);
    }
}

package com.ican.elearning.service;

import com.ican.elearning.dataobject.User;

/**
 * Created by JackyGuo
 * 2017/9/2 17:17
 */
public interface UserService {
    User login(String userAccount,String userPassword);
    User save(User user);
    User register(User user);
    User findByUserAccountAndUserPassword(String userAccount,String userPassword);
    User findByUserAccount(String userAccount);
    User findByUserId(String userId);
}

package com.ican.elearning.Dao;

import com.ican.elearning.dataobject.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by JackyGuo
 * 2017/9/1 15:32
 */
public interface UserDao extends JpaRepository<User,String> {
    User findByUserAccountAndUserPassword(String userAccount,String userPassword);
    User findByUserAccount(String userAccount);
}

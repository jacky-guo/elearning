package com.ican.elearning.controller;

import com.ican.elearning.VO.ResultVO;
import com.ican.elearning.VO.UserVO;
import com.ican.elearning.dataobject.User;
import com.ican.elearning.enums.ResultEnum;
import com.ican.elearning.exception.ElearningException;
import com.ican.elearning.form.UserForm;
import com.ican.elearning.service.UserService;
import com.ican.elearning.utils.KeyUtil;
import com.ican.elearning.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by JackyGuo
 * 2017/9/1 22:08
 */

@Slf4j
//包含@Controller 和 @ResponseBody 這兩個註解 用於實現restful web service
@RestController
//解決跨越問題
@CrossOrigin(origins = "*")
//根路由
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    //登錄
    @PostMapping("/login")
    public ResultVO<User> login(@RequestParam("username") String username,
                            @RequestParam("password") String password) {
        User user;
        UserVO userVO = new UserVO();
        try {
            user = userService.login(username, password);
        } catch (Exception e) {
            //如果有異常拋出錯誤訊息
            return ResultVOUtil.error(ResultEnum.UNAUTHORIZED.getCode(),e.getMessage());
        }
        //將使用者資訊copy到userVO回傳前端
        BeanUtils.copyProperties(user,userVO);
        //修改最後登入時間
        userService.save(user);
        return ResultVOUtil.success(userVO);
    }

    //註冊
    @PostMapping("/register")
    public ResultVO<User> register(@Valid UserForm userForm,
                                   BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                log.error("【用戶註冊】參數不正確，userForm={}", userForm);
                throw new ElearningException(ResultEnum.PARAM_ERROR.getCode(),
                        bindingResult.getFieldError().getDefaultMessage());
            }
        } catch (Exception e) {
            //如果有異常拋出錯誤訊息
            return ResultVOUtil.error(ResultEnum.BADREQUEST.getCode(),e.getMessage());
        }
        //TODO 需修改註冊時使用者等級和權限設定
        User user = new User();
        user.setUserAccount(userForm.getUsername());
        user.setUserPassword(userForm.getPassword());
        user.setUserLevel(600);
        user.setUserIdentity(1);
        user.setUserId(KeyUtil.genUniqueKey());

        User createResult;
        try {
            createResult = userService.register(user);
        } catch (Exception e) {
            //如果有異常拋出錯誤訊息
            return ResultVOUtil.error(ResultEnum.FORBIDDEN.getCode(),e.getMessage());
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(createResult,userVO);

        return ResultVOUtil.success(userVO);
    }
}

package com.ican.elearning.controller;

import com.ican.elearning.VO.ResultVO;
import com.ican.elearning.converter.ExamForm2ExamDTOConverter;
import com.ican.elearning.dataobject.User;
import com.ican.elearning.dto.ExamDTO;
import com.ican.elearning.enums.ResultEnum;
import com.ican.elearning.exception.ElearningException;
import com.ican.elearning.form.ExamForm;
import com.ican.elearning.service.ExamService;
import com.ican.elearning.service.UserService;
import com.ican.elearning.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


/**
 * Created by JackyGuo
 * 2017/10/3 21:46
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamService examService;
    @Autowired
    private UserService userService;

    @PostMapping("/insert")
    public ResultVO<ExamDTO> insert(ExamForm examForm) {

        ExamDTO examDTO = ExamForm2ExamDTOConverter.convert(examForm);
        //TODO 新增考卷
        System.out.println(examDTO.getQuestionDTOList().get(1).getAnswerDTOList().size());
        return null;
    }

    @GetMapping("/autoGenerateExam")
    public ResultVO<ExamDTO> autoGenerateExam(@RequestParam("grade") String examGrade,
                                              @RequestParam(value = "examNumber",defaultValue = "3") Integer examNum) {
        try {
            if (StringUtils.isEmpty(examGrade)) {
                log.error("【自動產生考卷】grade為空");
                throw new ElearningException(ResultEnum.PARAM_ERROR);
            }
        } catch (Exception e) {
            //如果有異常拋出錯誤訊息
            return ResultVOUtil.error(ResultEnum.BADREQUEST.getCode(),e.getMessage());
        }

        ExamDTO examDTO = examService.autoGenerateExam(examGrade, examNum);
        return ResultVOUtil.success(examDTO);
    }

    @GetMapping("/dynamicExam")
    public ResultVO<ExamDTO> dynamicExam(@RequestParam("userId") String userId,
                                         @RequestParam(value = "lastLevel",defaultValue = "0") Integer lastLevel,
                                         @RequestParam(value = "result", defaultValue = "True") String result) {
        try {
            if (StringUtils.isEmpty(userId)) {
                log.error("【互動測驗】userId為空");
                throw new ElearningException(ResultEnum.PARAM_ERROR);
            }
        } catch (Exception e) {
            //如果有異常拋出錯誤訊息
            return ResultVOUtil.error(ResultEnum.BADREQUEST.getCode(),e.getMessage());
        }
        User user =  userService.findByUserId(userId);
        if (lastLevel == 0) {
            lastLevel = user.getUserLevel();
        }
        ExamDTO examDTO = examService.dynamicExam(user,lastLevel,result);

        return ResultVOUtil.success(examDTO);
    }

    @GetMapping("/generalExam")
    public ResultVO<ExamDTO> generalExam(@RequestParam("grade") String examGrade,
                                         @RequestParam(value = "examNumber",defaultValue = "3") Integer examNum) {
        try {
            if (StringUtils.isEmpty(examGrade)) {
                log.error("【一般測驗】grade為空");
                throw new ElearningException(ResultEnum.PARAM_ERROR);
            }
        } catch (Exception e) {
            //如果有異常拋出錯誤訊息
            return ResultVOUtil.error(ResultEnum.BADREQUEST.getCode(),e.getMessage());
        }

        ExamDTO examDTO = examService.generalExam(examGrade, examNum);
        return ResultVOUtil.success(examDTO);
    }

}

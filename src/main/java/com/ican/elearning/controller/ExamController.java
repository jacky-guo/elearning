package com.ican.elearning.controller;

import com.ican.elearning.VO.ResultVO;
import com.ican.elearning.converter.ExamForm2ExamDTOConverter;
import com.ican.elearning.dto.ExamDTO;
import com.ican.elearning.enums.ResultEnum;
import com.ican.elearning.exception.ElearningException;
import com.ican.elearning.form.ExamForm;
import com.ican.elearning.service.ExamService;
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

    @PostMapping("/insert")
    public ResultVO<ExamDTO> insert(ExamForm examForm) {

        ExamDTO examDTO = ExamForm2ExamDTOConverter.convert(examForm);
        //TODO 新增考卷
        System.out.println(examDTO.getQuestionDTOList().get(1).getAnswerDTOList().size());
        return null;
    }


    @GetMapping("/autoGenerateExam")
    public ResultVO<ExamDTO> autoGenerateExam(@RequestParam("grade") String examGrade,
                                              @RequestParam(value = "examNumber",defaultValue = "3") Integer examNumber) {
        try {
            if (StringUtils.isEmpty(examGrade)) {
                log.error("【自動產生考卷】grade為空");
                throw new ElearningException(ResultEnum.PARAM_ERROR);
            }
        } catch (Exception e) {
            //如果有異常拋出錯誤訊息
            return ResultVOUtil.error(ResultEnum.BADREQUEST.getCode(),e.getMessage());
        }

        ExamDTO examDTO = examService.autoGenerateExam(examGrade, examNumber);

        return ResultVOUtil.success(examDTO);
    }

}

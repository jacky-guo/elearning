package com.ican.elearning.controller;

import com.ican.elearning.VO.ResultVO;
import com.ican.elearning.dataobject.LessonPlan;
import com.ican.elearning.service.StorageService;
import com.ican.elearning.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JackyGuo
 * 2017/9/5 15:30
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/file")
public class UploadController {

    @Autowired
    StorageService storageService;

    List<String> files = new ArrayList<String>();

    @PostMapping("/")
    public ResultVO<LessonPlan> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("planGrade") String planGrade,
                                                 @RequestParam("planLesson") String planLesson,
                                                 @RequestParam("planHashtag") String planHashtag,
                                                 @RequestParam("createBy") String createBy) {
        HashMap hashMap = new HashMap();
        hashMap.put("planGrade",planGrade);
        hashMap.put("planLesson",planLesson);
        hashMap.put("planHashtag",planHashtag);
        hashMap.put("createBy",createBy);
        try {
            storageService.store(file,hashMap);
            files.add(file.getOriginalFilename());
        } catch (Exception e) {
            return ResultVOUtil.error(403,e.getMessage());

        }
        return ResultVOUtil.success();
    }

    @GetMapping("/getfiles")
    public ResultVO<LessonPlan> getListFile(@RequestParam("grade") String planGrade,
                                            @RequestParam(value = "page", defaultValue = "0") Integer page,
                                            @RequestParam(value = "size", defaultValue = "20") Integer size) {

        PageRequest request = new PageRequest(page,size);
        Page<LessonPlan> lessonPlanPage = storageService.findByPlanGrade(planGrade,request);

        HashMap hashMap = new HashMap();
        hashMap.put("count",lessonPlanPage.getTotalElements());
        hashMap.put("data",lessonPlanPage.getContent());
//        List<LessonPlan> lessonPlanList = storageService.findAll();
//        model.addAttribute("files",
//                files.stream()
//                        .map(fileName -> MvcUriComponentsBuilder
//                                .fromMethodName(UploadController.class, "getFile", fileName).build().toString())
//                        .collect(Collectors.toList()));
//        model.addAttribute("totalFiles", "TotalFiles: " + files.size());
        return ResultVOUtil.success(hashMap);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping("/delete")
    public ResultVO<LessonPlan> delete(@RequestParam("planId") Integer planId) {
        storageService.delete(planId);
        return ResultVOUtil.success();
    }
}

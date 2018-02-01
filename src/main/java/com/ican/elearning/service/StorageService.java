package com.ican.elearning.service;

import com.ican.elearning.Dao.LessonPlanDao;
import com.ican.elearning.dataobject.LessonPlan;
import com.ican.elearning.enums.ResultEnum;
import com.ican.elearning.exception.ElearningException;
import com.ican.elearning.utils.KeyUtil;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JackyGuo
 * 2017/9/5 15:28
 */
@Service
public class StorageService {
    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final Path rootLocation = Paths.get("upload-dir");

    @Autowired
    private LessonPlanDao lessonPlanDao;

    public void store(MultipartFile file, HashMap hashMap){
        try {
            String planFileurl = KeyUtil.genUniqueKey()+file.getOriginalFilename();

            LessonPlan lessonPlan = new LessonPlan();
            lessonPlan.setCreateBy(hashMap.get("createBy").toString());
            lessonPlan.setPlanFilename(file.getOriginalFilename());
            lessonPlan.setPlanFileurl(planFileurl);
            lessonPlan.setPlanHashtag(hashMap.get("planHashtag").toString());
            lessonPlan.setPlanGrade(hashMap.get("planGrade").toString());
            lessonPlan.setPlanLesson(hashMap.get("planLesson").toString());

            Files.copy(file.getInputStream(), this.rootLocation.resolve(planFileurl));
            lessonPlanDao.save(lessonPlan);
        } catch (Exception e) {
            log.error("【上傳檔案】上傳錯誤,file={},hashMap={}",file,hashMap);
            throw new ElearningException(ResultEnum.UPLOAD_ERROR);
        }
    }

    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public List<LessonPlan> findAll() {
        List<LessonPlan> lessonPlanList = lessonPlanDao.findAll();
        return lessonPlanList;
    }

    public Page<LessonPlan> findByPlanGrade(String planGrade,Pageable pageable) {
        return lessonPlanDao.findByPlanGrade(planGrade,pageable);
    }

    public void delete(Integer planId) {
        lessonPlanDao.delete(planId);
    }

    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }
}

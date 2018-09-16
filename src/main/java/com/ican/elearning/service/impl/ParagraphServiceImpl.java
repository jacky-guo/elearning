package com.ican.elearning.service.impl;

import com.ican.elearning.Dao.ParagraphDao;
import com.ican.elearning.dataobject.Paragraph;
import com.ican.elearning.service.ParagraphService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by JackyGuo
 * 2017/9/4 2:42
 */
@Slf4j
@Service
public class ParagraphServiceImpl implements ParagraphService {

    @Autowired
    private ParagraphDao paragraphDao;

    @Override
    public List<Paragraph> findAll() {
        return paragraphDao.findAll();
    }

    @Override
    public Paragraph findByParagraphId(Integer paragraphId) {
        return paragraphDao.findOne(paragraphId);
    }

    @Override
    public Paragraph save(Paragraph paragraph) {
        return paragraphDao.save(paragraph);
    }

    @Override
    public List<Paragraph> findByParagraphGrade(String paragraphGrade) {
        return paragraphDao.findByParagraphGrade(paragraphGrade);
    }

    @Override
    public List<Paragraph> findByParagraphLevel(Integer paragraphLevel) {
        return paragraphDao.findByParagraphLevel(paragraphLevel);
    }

    @Override
    public Page<Paragraph> findByParagraphGrade(String paragraphGrade, Pageable pageable) {
        return paragraphDao.findByParagraphGrade(paragraphGrade,pageable);
    }

    @Override
    public void delete(Integer paragraphId) {
        paragraphDao.delete(paragraphId);
    }

    @Override
    public List<Paragraph> findByParagraphSourceIn(List<String> paragraphSourceList) {
        return paragraphDao.findByParagraphSourceIn(paragraphSourceList);
    }

    @Override
    public List<Paragraph> findByParagraphSource(String paragraphSource) {
        return paragraphDao.findByParagraphSource(paragraphSource);
    }

    @Override
    public List<Paragraph> findByCreateTimeBetween(Date dateBefore, Date dateNow) {
        return paragraphDao.findByCreateTimeBetween(dateBefore,dateNow);
    }

    @Override
    public Page<Paragraph> findByParagraphLevelBetween(Integer down, Integer up, Pageable pageable) {
        return paragraphDao.findByParagraphLevelBetween(down,up,pageable);
    }

    @Override
    public Page<Paragraph> findByParagraphHashtag(String paragraphHashtag, Pageable pageable) {
        return paragraphDao.findByParagraphHashtag(paragraphHashtag,pageable);
    }

    @Override
    public List<Paragraph> findByParagraphGradeOrderByCreateTimeDesc(String paragraphGrade) {
        return paragraphDao.findByParagraphGradeOrderByCreateTimeDesc(paragraphGrade);
    }
}

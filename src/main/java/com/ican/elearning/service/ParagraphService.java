package com.ican.elearning.service;

import com.ican.elearning.dataobject.Paragraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * Created by JackyGuo
 * 2017/9/4 2:30
 */
public interface ParagraphService {
    //查詢所有教材
    List<Paragraph> findAll();

    Paragraph findByParagraphId(Integer paragraphId);

    //新增教材
    Paragraph save(Paragraph paragraph);
    //根據年級查詢教材
    List<Paragraph> findByParagraphGrade(String paragraphGrade);
    Page<Paragraph> findByParagraphGrade(String paragraphGrade, Pageable pageable);
    //根據難易度查詢教材
    List<Paragraph> findByParagraphLevel(Integer paragraphLevel);

    void delete(Integer paragraphId);
    List<Paragraph> findByParagraphSourceIn(List<String> paragraphSourceList);
    List<Paragraph> findByParagraphSource(String paragraphSource);
    List<Paragraph> findByCreateTimeBetween(Date dateBefore, Date dateAfter);
    Page<Paragraph> findByParagraphLevelBetween(Integer down,Integer up, Pageable pageable);
    Page<Paragraph> findByParagraphHashtag(String paragraphHashtag,Pageable pageable);

}

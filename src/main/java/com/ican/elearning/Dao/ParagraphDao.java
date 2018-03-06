package com.ican.elearning.Dao;

import com.ican.elearning.dataobject.Paragraph;
import org.python.antlr.op.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;

import java.util.List;

/**
 * Created by JackyGuo
 * 2017/9/4 2:27
 */
public interface ParagraphDao extends JpaRepository<Paragraph,Integer> {
    List<Paragraph> findByParagraphGrade(String paragraphGrade);
    List<Paragraph> findByParagraphLevel(Integer paragraphLevel);
    Page<Paragraph> findByParagraphGrade(String paragraphGrade, Pageable pageable);
    List<Paragraph> findByParagraphSourceIn(List<String> paragraphSourceList);
    List<Paragraph> findByParagraphSource(String paragraphSource);
    List<Paragraph> findByCreateTimeBetween(Date dateBefore,Date dateAfter);
    Page<Paragraph> findByParagraphLevelBetween(Integer down,Integer up, Pageable pageable);
    Page<Paragraph> findByParagraphHashtag(String paragraphHashtag,Pageable pageable);
    Paragraph findByParagraphId(Integer paragraphId);
}

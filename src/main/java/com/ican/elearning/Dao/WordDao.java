package com.ican.elearning.Dao;

import com.ican.elearning.dataobject.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by JackyGuo
 * 2017/9/1 14:39
 */
public interface WordDao extends JpaRepository<Word,Integer> {
    List<Word> findByWordLevelIn(List<Integer> wordLevelList);
    List<Word> findByWordGradeIn(List<String> wordGradeList);
    List<Word> findByWordGrade(String wordGrade);
    Page<Word> findByWordGrade(String wordGrade, Pageable pageable);
    List<Word> findByWordContentIn(List<String> wordContentList);
    List<Word> findByWordContent(String wordContent);
    List<Word> findByWordContentAndWordPartofspeech(String wordContent,String wordPartofspeech);
    List<Word> findByWordContentInAndWordGrade(List<String> wordContentList,String wordGrade);
    List<Word> findByWordPartofspeech(String wordPartofspeech);
    Page<Word> findByWordLevelBetween(Integer down,Integer up, Pageable pageable);
    Page<Word> findByWordContentStartingWith(String wordContent,Pageable pageable);
}

package com.ican.elearning.service;

import com.ican.elearning.dataobject.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by JackyGuo
 * 2017/9/1 15:50
 */
public interface WordService {
    //新增和更新都是Save方法
    Word save(Word word);

    //根據單字ID查找單字
    Word findOne(Integer wordId);

    //查詢所有單字列表
    List<Word> findAll();

    //根據難易度查詢單字
    List<Word> findByWordLevelIn(List<Integer> wordLevelList);

    //根據年級查詢單字
    List<Word> findByWordGradeIn(List<String> wordGradeList);
    Page<Word> findByWordGrade(String wordGrade,Pageable pageable);

    //根據單字內容查詢單字
    List<Word> findByWordContentIn(List<String> wordContentList);
    List<Word> findByWordContentAndWordPartofspeech(String wordContent,String wordPartofspeech);
    List<Word> findByWordContentInAndWordGrade(List<String> wordContentList,String wordGrade);
    List<Word> findByWordPartofspeech(String wordPartofspeech);
    List<Word> findByWordContent(String wordContent);
    Page<Word> findByWordLevelBetween(Integer down,Integer up, Pageable pageable);
    Page<Word> findByWordContentStartingWith(String wordContent,Pageable pageable);


    void delete(Integer wordId);


}

package com.ican.elearning.service.impl;

import com.ican.elearning.Dao.WordDao;
import com.ican.elearning.dataobject.Word;
import com.ican.elearning.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by JackyGuo
 * 2017/9/1 15:59
 */
@Service
public class WordServiceImpl implements WordService {

    @Autowired
    private WordDao wordDao;

    @Override
    public Word save(Word word) {
        return wordDao.save(word);
    }

    @Override
    public Word findOne(Integer wordId) {
        return wordDao.findOne(wordId);
    }

    @Override
    public List<Word> findAll() {
        return wordDao.findAll();
    }

    @Override
    public List<Word> findByWordLevelIn(List<Integer> wordLevelList) {
        return wordDao.findByWordLevelIn(wordLevelList);
    }

    @Override
    public List<Word> findByWordGradeIn(List<String> wordGradeList) {
        return wordDao.findByWordGradeIn(wordGradeList);
    }

    @Override
    public List<Word> findByWordContentIn(List<String> wordContentList) {
        return wordDao.findByWordContentIn(wordContentList);
    }

    @Override
    public List<Word> findByWordGrade(String wordGrade) {
        return wordDao.findByWordGrade(wordGrade);
    }

    @Override
    public Page<Word> findByWordGrade(String wordGrade,Pageable pageable) {
//        Page<Word> wordList = wordDao.findByWordGrade(wordGrade,pageable);
//        return new PageImpl<Word>(wordList.getContent(),pageable,wordList.getTotalElements());
        return wordDao.findByWordGrade(wordGrade,pageable);
    }

    @Override
    public void delete(Integer wordId) {
        wordDao.delete(wordId);
    }

    @Override
    public List<Word> findByWordContentInAndWordGrade(List<String> wordContentList, String wordGrade) {
        return wordDao.findByWordContentInAndWordGrade(wordContentList,wordGrade);
    }

    @Override
    public List<Word> findByWordPartofspeech(String wordPartofspeech) {
        return wordDao.findByWordPartofspeech(wordPartofspeech);
    }

    @Override
    public List<Word> findByWordContent(String wordContent) {
        return wordDao.findByWordContent(wordContent);
    }

    @Override
    public List<Word> findByWordContentAndWordPartofspeech(String wordContent, String wordPartofspeech) {
        return wordDao.findByWordContentAndWordPartofspeech(wordContent,wordPartofspeech);
    }

    @Override
    public Page<Word> findByWordLevelBetween(Integer down, Integer up,Pageable pageable) {
        return wordDao.findByWordLevelBetween(down,up,pageable);
    }

    @Override
    public Page<Word> findByWordContentStartingWith(String wordContent, Pageable pageable) {
        return wordDao.findByWordContentStartingWith(wordContent,pageable);
    }
}

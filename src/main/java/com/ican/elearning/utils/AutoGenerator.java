package com.ican.elearning.utils;

import com.ican.elearning.dataobject.Paragraph;
import com.ican.elearning.dataobject.Word;
import com.ican.elearning.dto.QuestionDTO;

import java.util.*;

/**
 * Created by JackyGuo
 * 2017/9/17 15:18
 */
public class AutoGenerator {

    public static List<Paragraph> searchFitnessParagraph(List<Paragraph> paragraphList, List<Word> wordList,Integer paragraphNumber) {
        Set<String> dbwords = new HashSet<>();
        for(Word word:wordList) {
            dbwords.add(word.getWordContent());
        }
        Map<Paragraph,Integer> wordCountMap = new LinkedHashMap<>();
        for(Paragraph paragraph:paragraphList) {
            //TODO 做了多餘動作 後面可將函數抽出
            //將句子分解成單字
            Set<String> paragraphWords = Lemmatizer.lemma(paragraph.getParagraphContent()).keySet();
            //retainAll取交集
            paragraphWords.retainAll(dbwords);
            wordCountMap.put(paragraph,paragraphWords.size());

        }
        List<Map.Entry> entryList = new ArrayList<>(wordCountMap.entrySet());
        //對Map按可出題數排序
        Comparator< Map.Entry> sortByValue = (e1,e2)->{ return ((Integer)e1.getValue()).compareTo( (Integer)e2.getValue()); };
        Collections.sort(entryList, sortByValue );
        Collections.reverse(entryList);

        List<Paragraph> result = new ArrayList<>();
        for(int i=0;i<paragraphNumber;i++) {
            Map.Entry e = entryList.get(i);
//            System.out.println(e.getValue());
//            result.put( (Paragraph)e.getKey(), (Integer)e.getValue());
            result.add((Paragraph)e.getKey());
        }
//        System.out.println(result);
        return result;
    }

    public static List<QuestionDTO> autoGenerator(List<Paragraph> paragraphList, List<Word> wordList,Integer paragraphNumber){

        List<QuestionDTO> result = new ArrayList<>();
        return result;
    }
}

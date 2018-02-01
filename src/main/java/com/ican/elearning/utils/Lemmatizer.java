package com.ican.elearning.utils;

import com.ican.elearning.dataobject.Paragraph;
import com.ican.elearning.dto.AnswerDTO;
import com.ican.elearning.dto.QuestionDTO;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;


import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by JackyGuo
 * 2017/9/15 16:12
 */
public class Lemmatizer {

    public static Integer ssplit(String doc) {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation document = new Annotation(doc);
        pipeline.annotate(document);
        return document.get(CoreAnnotations.SentencesAnnotation.class).size();

    }
    //回傳(key:原形,value:詞性)的 map
    public static Map<String, String> lemma(String doc) {
        //將sentecesParser解析后的格式重新打包，過濾標點符號和重複的單字。
        List<Map<String, String>> mapList = sentencesParser(doc);
        Map<String, String> resultMap = new LinkedHashMap<>();
        for (Map<String,String> wordMap:mapList) {
            String pos = wordMap.get("pos");
            String lemma = wordMap.get("lemma");
            //過濾字串中的 # $ . , : ( ) ' ' " " 符號和Penn Treebank中的UH|SYM|POS|CD|FW|LS|RP
            if ((resultMap.get(lemma) == null) && (!pos.matches(".|,|:|#|$|-LRB-|-RRB-|''|``|UH|SYM|POS|CD|FW|LS|RP"))) {
                resultMap.put(lemma, pos);
            }
        }
        return resultMap;
    }
    //段落分析的核心
    public static List<Map<String, String>> sentencesParser(String doc) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        // create an empty Annotation just with the given text
        //在這裡輸入doc
        Annotation document = new Annotation(doc);
        // run all Annotators on this text
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        List<Map<String, String>> wordList = new ArrayList<>();

        //Penn Treebank regular expression pattern
        Pattern nounP = Pattern.compile("^NN");
        Pattern verbP = Pattern.compile("^VB");
        Pattern adjP = Pattern.compile("^JJ");
        Pattern advP = Pattern.compile("RB|EX");
        Pattern pronP = Pattern.compile("^WP|PP$|PR$|WDT");

        //每個句子
        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            //每句中的每個單字
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {

                Map<String, String> wordMap = new LinkedHashMap<>();
                //單字在句子中的形態
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                //單字原形
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                //單字的時態
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

                //將Penn Treebank轉換成資料庫中的詞性
                if (nounP.matcher(pos).find()) {
                    pos = "noun";
                } else if (verbP.matcher(pos).find()) {
                    pos = "verb";
                } else if (adjP.matcher(pos).find()) {
                    pos = "adjective";
                } else if (advP.matcher(pos).find()) {
                    pos = "adverb";
                } else if (pos.matches("CC")) {
                    pos = "conjunction";
                } else if (pos.matches("IN|TO")) {
                    pos = "preposition";
                } else if (pos.matches("DT|PDT")) {
                    pos = "article";
                } else if (pos.matches("MD")) {
                    pos = "auxiliary-verb";
                } else if (pronP.matcher(pos).find()) {
                    pos = "pronoun";
                }

                wordMap.put("content", word);
                wordMap.put("lemma", lemma);
                wordMap.put("pos", pos);
                wordList.add(wordMap);
            }
        }
        return wordList;
    }

//    public static void fullReplace(Map<String, String> wordList,) {
//        for (Map<String, String> word : wordList) {
//            List<Word> samePosWordList = samePosWordMap.get(word.get("pos"));
//            samePosWordList.remove(wordMap.get(word.get("lemma")));
//            List<String> falseAnsList = new ArrayList<>();
//            if (samePosWordList.size() >= 4) {
//                for (int i = 0; i < 3; i++) {
//                    int j = (int) (Math.random() * samePosWordList.size());
//                    falseAnsList.add(samePosWordList.get(j).getWordContent());
//                    samePosWordList.remove(j);
//                }
//            } else {
//
//            }
//            Map<String, Object> ansMap = new HashMap<>();
//            ansMap.put("content", word.get("content"));
//            ansMap.put("falseAnsList", falseAnsList);
//            resultList.add(ansMap);
//        }
//
//    }

    public static QuestionDTO someReplace(Paragraph paragraph,Map<String,String> dbWordMap) {

        //考卷DTO初始化
        QuestionDTO questionDTO = new QuestionDTO();
        //TODO 考卷難易度定義
        questionDTO.setQuestionLevel(Integer.valueOf(paragraph.getParagraphGrade()) * 100);
//        questionDTO.setQuestionLevel(paragraph.getParagraphLevel());
        //TODO 抽成enum
        questionDTO.setQuestionType(2);
        questionDTO.setQuestionAutoCreate(1);
        questionDTO.setCreateBy("AutoGenerate");

        questionDTO.setQuestionGrade(paragraph.getParagraphGrade());
        questionDTO.setQuestionParagraphId(paragraph.getParagraphId());
        questionDTO.setQuestionSource(paragraph.getParagraphSource());
        questionDTO.setQuestionHashtag(paragraph.getParagraphHashtag());

        //解析教材
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(paragraph.getParagraphContent());
        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        List<AnswerDTO> answerDTOList = new ArrayList<>();
        //挖空的序號
        int index = 0;
        String questionContent = "";
        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            //控制一個句子最多只挖一個空
            boolean flag = false;
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

                if (dbWordMap.keySet().contains(lemma)&&flag==false) {
                    word = "_" + (index + 1) + "_";
                    //挖空序號
                    flag = true;
                    index ++;
                    //避免考到重複的單字
                    dbWordMap.remove(lemma);

                    //錯誤答案集
                    List<String> wrongAnswer = new ArrayList<>(dbWordMap.keySet());
                    //TODO 目前使用方法為隨機給錯誤答案。
                    AnswerDTO answerDTO = new AnswerDTO();
                    answerDTO.setTitle(String.valueOf(index));
                    answerDTO.setRightvalue(lemma);
                    int value1 =(int)(Math.random()*wrongAnswer.size());
                    answerDTO.setWrongvalue1(wrongAnswer.get(value1));
                    wrongAnswer.remove(value1);
                    int value2 =(int)(Math.random()*wrongAnswer.size());
                    answerDTO.setWrongvalue2(wrongAnswer.get(value2));
                    wrongAnswer.remove(value2);
                    int value3 =(int)(Math.random()*wrongAnswer.size());
                    answerDTO.setWrongvalue3(wrongAnswer.get(value3));
                    answerDTOList.add(answerDTO);
                }
                //處理拼接時空格問題
                if ((!pos.matches(".|,|:|#|$|-LRB-|-RRB-|''|``"))) {
                    questionContent = questionContent + " " + word;
                }else {
                    if (pos.matches("-LRB-"))
                        word = "(";
                    if (pos.matches("-RRB-"))
                        word = ")";
                    questionContent = questionContent + word;
                }

            }
        }
        questionDTO.setQuestionContent(questionContent);
        questionDTO.setAnswerDTOList(answerDTOList);
        if (index ==0 ) {
            return null;
        }else {
            return questionDTO;
        }
    }


    public static void main(String[] args) {

//        String text = "A bus is driving on a highway.Mr. Wang don't break up EQ. Please, come down. It's a church bus. It is in Arkansas, USA. A girl is on the bus. She sits at a door of the bus.";
//
//        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
//        Properties props = new Properties();
//        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//
//        // create an empty Annotation just with the given text
//        Annotation document = new Annotation(text);
//        // run all Annotators on this text
//        pipeline.annotate(document);
//
//        Map<String, String> wordMap = new LinkedHashMap<>();
//        List<String> wordList = new ArrayList<>();
//
//        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
//
//        for (CoreMap sentence : sentences) {
//            // traversing the words in the current sentence
//            // a CoreLabel is a CoreMap with additional token-specific methods
//
//            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
//                // this is the text of the token
//                String word = token.get(CoreAnnotations.TextAnnotation.class);
//                // this is the POS tag of the token
//                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
//                // this is the NER label of the token
//                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
//
//                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
//
//                wordMap.put(word, pos);
//                wordList.add(word);
//
//                System.out.println(String.format("Print: word:[%s] pos: [%s] ne: [%s] lemma: [%s]", word, pos, ne, lemma));
//            }
//
//
////            // this is the parse tree of the current sentence
////            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
////            System.out.println("parse tree:\n" + tree);
////
////            // this is the Stanford dependency graph of the current sentence
////            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
////            System.out.println("dependency graph:\n" + dependencies);
//
//        }
//
//        wordList.set(5, "abc");
//
//        for (int i = 0; i < wordList.size(); i++) {
//            System.out.print(wordList.get(i) + " ");
//        }
//        System.out.println(wordList.size());
    }

}

package com.ican.elearning.utils;

import com.ican.elearning.dataobject.Paragraph;
import com.ican.elearning.dataobject.Word;
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
import org.python.google.common.collect.HashMultimap;
import org.python.google.common.collect.Multimap;


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
    public static List<QuestionDTO> autoGenerator(List<Paragraph> paragraphList, List<Word> wordList, Integer paragraphNumber) {

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        Map<String,String> dbWordMap = new LinkedHashMap<>();
        for (Word word:wordList) {
            dbWordMap.put(word.getWordContent(),word.getWordPartofspeech());
        }

        Pattern nounP = Pattern.compile("^NN");
        Pattern verbP = Pattern.compile("^VB");
        Pattern adjP = Pattern.compile("^JJ");
        Pattern advP = Pattern.compile("RB|EX");
        Pattern pronP = Pattern.compile("^WP|PP$|PR$|WDT");

        for (int questionNumber = 0; questionNumber < paragraphNumber; questionNumber ++) {

            //選教材
            int selectNumber = (int)(Math.random()*(paragraphList.size() - questionNumber));
            Paragraph paragraph = paragraphList.get(selectNumber);

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
                    String correct = word;

                    if (dbWordMap.keySet().contains(lemma)&&flag==false) {
                        index ++;
                        word = "_" + index + "_";
                        //挖空序號
                        flag = true;
                        //避免考到重複的單字
                        dbWordMap.remove(lemma);

                        //錯誤答案集
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

                        Multimap<String, String> multiMap = HashMultimap.create();
                        for (Map.Entry<String, String> entry : dbWordMap.entrySet()) {
                            multiMap.put(entry.getValue(), entry.getKey());
                        }
                        Collection<String> samePosWord = multiMap.asMap().get(pos);
                        List<String> wrongAnswer;
                        if (samePosWord.size() > 3) {
                            wrongAnswer = new ArrayList<>(samePosWord);
                        }else {
                            wrongAnswer = new ArrayList<>(dbWordMap.keySet());
                        }

                        AnswerDTO answerDTO = new AnswerDTO();
                        answerDTO.setTitle(String.valueOf(index));
                        answerDTO.setAnswerOrders(index);
                        List<String> answerContent = new ArrayList<>();
                        answerContent.add(correct);
                        answerDTO.setCorrect(correct);
                        int value1 =(int)(Math.random()*wrongAnswer.size());
                        answerContent.add(wrongAnswer.get(value1));
                        wrongAnswer.remove(value1);
                        int value2 =(int)(Math.random()*wrongAnswer.size());
                        answerContent.add(wrongAnswer.get(value2));
                        wrongAnswer.remove(value2);
                        int value3 =(int)(Math.random()*wrongAnswer.size());
                        answerContent.add(wrongAnswer.get(value3));
                        // shuffle option order
                        Collections.shuffle(answerContent);
                        answerDTO.setAnswerContent(answerContent);
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
            //考卷DTO初始化
            QuestionDTO questionDTO = new QuestionDTO();
            //考卷難易度定義
            questionDTO.setQuestionLevel(Integer.valueOf(paragraph.getParagraphGrade()) * 100);
            questionDTO.setQuestionType(2);
            questionDTO.setQuestionAutoCreate(1);
            questionDTO.setCreateBy("AutoGenerate");

            questionDTO.setQuestionGrade(paragraph.getParagraphGrade());
            questionDTO.setQuestionParagraphId(paragraph.getParagraphId());
            questionDTO.setQuestionSource(paragraph.getParagraphSource());
            questionDTO.setQuestionHashtag(paragraph.getParagraphHashtag());

            questionDTO.setQuestionContent(questionContent);
            questionDTO.setAnswerDTOList(answerDTOList);

            paragraphList.remove(selectNumber);

            if (index ==0 ) {
                questionNumber --;
            }else {
                questionDTOList.add(questionDTO);
            }
        }

        return questionDTOList;
    }

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
                String correct = word;

                if (dbWordMap.keySet().contains(lemma)&&flag==false) {
                    index ++;
                    word = "_" + index + "_";
                    //挖空序號
                    flag = true;
                    //避免考到重複的單字
                    dbWordMap.remove(lemma);

                    //錯誤答案集
                    List<String> wrongAnswer = new ArrayList<>();
                    //TODO 目前使用方法為隨機給錯誤答案。

                    AnswerDTO answerDTO = new AnswerDTO();
                    answerDTO.setTitle(String.valueOf(index));
                    answerDTO.setAnswerOrders(index);
                    List<String> answerContent = new ArrayList<>();
                    answerContent.add(correct);
                    answerDTO.setCorrect(correct);
                    int value1 =(int)(Math.random()*wrongAnswer.size());
                    answerContent.add(wrongAnswer.get(value1));
                    wrongAnswer.remove(value1);
                    int value2 =(int)(Math.random()*wrongAnswer.size());
                    answerContent.add(wrongAnswer.get(value2));
                    wrongAnswer.remove(value2);
                    int value3 =(int)(Math.random()*wrongAnswer.size());
                    answerContent.add(wrongAnswer.get(value3));
                    // shuffle option order
                    Collections.shuffle(answerContent);
                    answerDTO.setAnswerContent(answerContent);
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
/*
articles
-------------------------------
DT	限定词 Determiners|Articles
QT	量词 Quantifiers
CD	基数
WDT	Wh 限定词，例如 Which book do you like better 句子中的 which
--------------------------------
noun
--------------------------------
NN	名词（单数）
NNS	名词（复数）
NNP	专有名词（单数）
NNPS	专有名词（复数）
--------------------------------
pronoun
--------------------------------
EX	表示存在性的 there，例如在 There was a party 句子中。
PRP	人称代词 (PP)
PRP$	物主代词 (PP$)
WP	Wh 代词，例如用作关系代词的 which 和 that
WP$	wh 物主代词，例如 whose
--------------------------------
adverb
--------------------------------
RBS	副词（最高级）
RBR	副词（比较级）
RB	副词
WRB	Wh 副词，例如 I like it when you make dinner for me 句子中的 when
--------------------------------
adjective
--------------------------------
JJS	形容词（最高级）
JJR	形容词（比较级）
JJ	形容词
--------------------------------
auxiliary-verb
--------------------------------
MD	情态动词
--------------------------------
verb
--------------------------------
VB	动词（基本形式）
VBP	动词（现在时态，非第三人称单数）
VBZ	动词（现在时态，第三人称单数）
VBD	动词（过去时态）
VBN	动词（过去分词）
VBG	动词（动名词或现在分词）
--------------------------------
preposition
--------------------------------
TO	介词 to
IN	介词或从属连词
--------------------------------
conjunction
--------------------------------
CC	并列连词
--------------------------------
--------------------------------
POS	所有格结束词
UH	感叹词
RP	小品词
SYM	符号
$	货币符号
''	双引号或单引号
(	左圆括号、左方括号、左尖括号或左花括号
)	右圆括号、右方括号、右尖括号或右花括号
,	逗号
.	句末标点符号 (. ! ?)
:	句中标点符号 (: ; ... -- -)
 */

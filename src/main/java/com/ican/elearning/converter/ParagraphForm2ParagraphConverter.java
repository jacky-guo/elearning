package com.ican.elearning.converter;

import com.ican.elearning.dataobject.Paragraph;
import com.ican.elearning.form.ParagraphForm;
import com.ican.elearning.utils.Lemmatizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * Created by JackyGuo
 * 2017/9/4 15:37
 */
@Slf4j
public class ParagraphForm2ParagraphConverter {

    public static Paragraph convert(ParagraphForm paragraphForm) {
        Paragraph paragraph = new Paragraph();
        BeanUtils.copyProperties(paragraphForm,paragraph);
        //用空白斷詞
        Integer paragraphWordCount = paragraphForm.getParagraphContent().split(" ").length;
        //用正則表達式 [ . ? ! ; ] 用4個符號斷句
//        Integer paragraphSentenceLength = paragraphForm.getParagraphContent().split("[.?;!]").length;
        Integer paragraphSentenceLength = Lemmatizer.ssplit(paragraphForm.getParagraphContent());

        if (paragraphForm.getParagraphLevel() == null) {
            Integer paragraphLevel = Integer.valueOf(paragraphForm.getParagraphGrade()) * 100;
            paragraph.setParagraphLevel(paragraphLevel);
        }
        paragraph.setParagraphWordCount(paragraphWordCount);
        paragraph.setParagraphSentenceLength(paragraphSentenceLength);
        return paragraph;
    }

}



package com.ican.elearning.pipeline;

import com.ican.elearning.dataobject.Paragraph;
import com.ican.elearning.enums.ResultEnum;
import com.ican.elearning.service.ParagraphService;
import com.ican.elearning.utils.RestTemplate;
import com.ican.elearning.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;

/**
 * Created by JackyGuo
 * 2017/9/28 15:52
 */
@Repository
public class ParagraphPipeline implements Pipeline{

    @Autowired
    private ParagraphService paragraphService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            Paragraph paragraph = (Paragraph)entry.getValue();
            String paragraphSource = paragraph.getParagraphSource();
            //檢查鏈接是否已存在
            List<Paragraph> paragraphList = paragraphService.findByParagraphSource(paragraphSource);
            if (paragraphList.isEmpty() || paragraphList == null) {
                if (paragraph.getParagraphGrade() == null) {
                    try {
                        paragraph = RestTemplate.getParagraphLevel(paragraph);
                    } catch (Exception e) {
                        System.out.println(ResultVOUtil.error(ResultEnum.PYTHONSERVER_ERROR.getCode(),ResultEnum.PYTHONSERVER_ERROR.getMessage()));
                    }
                }
                Paragraph paragraphResult = paragraphService.save(paragraph);
                //自動產生題目
            }
        }
    }
}

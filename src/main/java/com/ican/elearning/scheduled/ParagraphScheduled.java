package com.ican.elearning.scheduled;

import com.ican.elearning.pipeline.ParagraphPipeline;
import com.ican.elearning.processor.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

/**
 * Created by JackyGuo
 * 2017/9/30 0:30
 */
@Component
public class ParagraphScheduled {
    @Autowired
    private ParagraphPipeline paragraphPipeline;

    @Scheduled(cron = "0 0 0 * * ? ")//每天0點執行
    public void ParagraphScheduled() {
        System.out.println("----開始執行定時任務");
        Spider spider = Spider.create(new Processor());
        //入口網址
        spider.addUrl("https://www.newsinlevels.com/page/1");
//        spider.addUrl("https://www.newsinlevels.com/?s=halloween");
        spider.addPipeline(paragraphPipeline);
        spider.thread(5);
        spider.setExitWhenComplete(true);
        spider.start();
    }

}

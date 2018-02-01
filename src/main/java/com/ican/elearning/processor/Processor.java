package com.ican.elearning.processor;

import com.ican.elearning.converter.ParagraphForm2ParagraphConverter;
import com.ican.elearning.dataobject.Paragraph;
import com.ican.elearning.form.ParagraphForm;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * Created by JackyGuo
 * 2017/9/4 19:51
 */
public class Processor implements PageProcessor {

    //setRetryTimes(int)	設置重試次數
    //setSleepTime(毫秒)    設置抓取間隔
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    //列表
    public static final String url_list = "https://www\\.newsinlevels\\.com/page/\\d+";
//    public static final String url_list = "https://www\\.newsinlevels\\.com/\\?s\\=halloween";
    //文章
    public static final String url_post = "https://www\\.newsinlevels\\.com/products/.*";

    @Override
    public void process(Page page) {

        //列表頁
        if (page.getUrl().regex(url_list).match()) {
//            page.addTargetRequests(page.getHtml().css("div.fancy-buttons").links().regex(url_post).all());//level 1-3 都會爬
            page.addTargetRequests(page.getHtml().css("div.title").links().regex(url_post).all());
            page.addTargetRequests(page.getHtml().links().regex(url_list).all());
            List<String> url = page.getHtml().css("ul.pagination").links().regex(url_list).all();
        } else {
            //Jsoup無法使用xpath的 position()和last()函數 所以用下面的方法實現content的去頭去尾
            List<String> all = page.getHtml().xpath("//div[@id='nContent']/p").all();
            StringBuilder build = new StringBuilder();
            for (String item : all.subList(1, all.size() - 2)) {
                build.append(item);
            }
            ParagraphForm paragraphForm = new ParagraphForm();
            //TODO 爬蟲的段落分級
            paragraphForm.setCreateBy("Admin");
            paragraphForm.setParagraphGrade("6");
            paragraphForm.setParagraphHashtag(page.getHtml().xpath("//div[@class='article-title']/h2/text()").toString());
            paragraphForm.setParagraphContent(Html.create(build.toString()).xpath("/tidyText()").get());
            paragraphForm.setParagraphSource(page.getUrl().toString());
            Paragraph paragraph = ParagraphForm2ParagraphConverter.convert(paragraphForm);
            //進入Pipeline
            if (!paragraph.getParagraphContent().isEmpty() && paragraph.getParagraphContent()!= null) {
                page.putField("content", paragraph);
            }
        }

    }

    @Override
    public Site getSite() {
        return site;
    }

    public void main() {
//        Spider.create(new Processor())
//                .addUrl("https://www.newsinlevels.com/page/1")
////                .addPipeline(new JsonFilePipeline("C:\\Users\\Administrator\\Desktop\\elearning\\webmagic"))
////                .addPipeline(new ParagraphPipeline(paragraphService))
//                .run();

    }
}

package com.ican.elearning.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by JackyGuo
 * 2017/9/10 15:18
 */
@Data
public class WordVO {
    @JsonProperty("id")
    private Integer wordId;
    @JsonProperty("content")
    private String wordContent;
    @JsonProperty("partOfSpeech")
    private String wordPartofspeech;
    @JsonProperty("interpretation")
    private String wordInterpretation;
    @JsonProperty("level")
    private Integer wordLevel;
}

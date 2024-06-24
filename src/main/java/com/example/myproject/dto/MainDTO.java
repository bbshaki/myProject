package com.example.myproject.dto;

import com.querydsl.core.annotations.QueryProjection;
import org.springframework.data.jpa.repository.Query;

public class MainDTO {

    private Long ano;
    private Long fno;
    private String title;
    private String content;
    private String imgUrl;

    @QueryProjection
    public MainDTO(Long ano, Long fno, String title, String content, String imgUrl){
        this.ano = ano;
        this.fno = fno;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
    }
}

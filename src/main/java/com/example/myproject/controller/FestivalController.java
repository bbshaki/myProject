package com.example.myproject.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/festival/")
public class FestivalController {

    @GetMapping("/write")
    public String fesWrite(){
        return "festival/write";
    }

    @GetMapping("/list")
    public String fesList(){
        return "festival/list";
    }

    @GetMapping("/read")
    public String fesRead(){
        return "festival/read";
    }

    @GetMapping("/modify")
    public String fesModify(){
        return "festival/modify";
    }
}

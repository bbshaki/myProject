package com.example.myproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MainController {

    @GetMapping("/")
    public String main(){
        return "main";
    }

    @GetMapping("/test/abc")
    public void test(){

    }

    @GetMapping("/test/aaa")
    public void test1(){

    }

    @GetMapping("/test/bbb")
    public void test2(){

    }
}

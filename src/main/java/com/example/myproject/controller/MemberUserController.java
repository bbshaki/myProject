package com.example.myproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/members/")
public class MemberUserController {

    @GetMapping("/register")
    public String memberRegister(){
        return "members/register";
    }

    @PostMapping("/register")
    public String registerPost(){
        return "main";
    }

    @GetMapping("/list")
    public String memberList(){
        return "members/list";
    }

    @GetMapping("/login")
    public String login(){
        return "members/login";
    }

    @GetMapping("/findID")
    public String findId(){
        return "members/findID";
    }

    @GetMapping("/findPw")
    public String findPw(){
        return "members/findPw";
    }

    @GetMapping("/read")
    public String read(){
        return "members/read";
    }

    @GetMapping("/modify")
    public String memberModify(){
        return "members/modify";
    }

    @GetMapping("/todo")
    public String myTodo(){
        return "members/todo";
    }


    }



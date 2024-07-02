package com.example.myproject.controller;

import com.example.myproject.dto.MemberUserDTO;
import com.example.myproject.dto.TodoDTO;
import com.example.myproject.entity.MemberUser;
import com.example.myproject.entity.Todo;
import com.example.myproject.service.MemberUserService;
import com.example.myproject.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/members/")
public class MemberUserController {

    private final PasswordEncoder passwordEncoder;
    private final MemberUserService memberUserService;
    private final TodoService todoService;

    @GetMapping("/register")
    public String memberRegister(Model model){
        model.addAttribute("memberUserDTO", new MemberUserDTO());
        return "/members/register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid MemberUserDTO memberUserDTO, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "/members/register";
        }

        try {
            MemberUser memberUser = MemberUser.createMember(memberUserDTO, passwordEncoder);
            memberUserService.saveMember(memberUser);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "/members/register";
        }
        return "redirect:/";
    }

    @GetMapping("/list")
    public String memberList(){
        return "members/list";
    }

    @GetMapping("/login")
    public String login(){

        return "/members/login";
    }

    @GetMapping("/login/error")
    public String loginError(Model model){

        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");

        return "/members/login";
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

    @GetMapping("/todoList")
    public String todoList(Model model, Principal principal){
        List<TodoDTO> tododtoList = todoService.todoDTOList(principal);
        model.addAttribute("todoList", tododtoList);
        tododtoList.forEach(a -> log.info(a));
        return "members/todoList";
    }

    @GetMapping("/modify")
    public String memberModify(){
        return "members/modify";
    }

    @GetMapping("/todo")
    public String myTodo(){
        return "todoList";
    }


    }



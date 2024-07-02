package com.example.myproject.controller;

import com.example.myproject.dto.TodoDTO;
import com.example.myproject.repository.TodoRepository;
import com.example.myproject.service.AttractionService;
import com.example.myproject.service.FestivalService;
import com.example.myproject.service.MemberUserService;
import com.example.myproject.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo/")
@Log4j2
public class TodoController {

    private final TodoRepository todoRepository;
    private final TodoService todoService;

    @PostMapping("/new")
    public Map<String, String> newTodo(@RequestBody TodoDTO todoDTO, Principal principal){
        todoDTO.setMno(todoDTO.getMno());
        Long tno = todoService.addTodo(todoDTO, principal);
        Map<String, String> result = new HashMap<>();
        result.put("tno", Long.toString(tno));
        return result;
    }

}

package com.example.myproject.controller;

import com.example.myproject.dto.FestivalDTO;
import com.example.myproject.entity.Festival;
import com.example.myproject.service.FestivalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CalendarController {

    private final FestivalService festivalService;

    @GetMapping("/calendar")
    public String calender(Model model) {
        List<FestivalDTO> festivalDTOList = festivalService.selectAll();
        model.addAttribute("festivalDTO", festivalDTOList);
        return "calendar";
    }

    @GetMapping("/calendar/aaa")
    public @ResponseBody ResponseEntity calender() {
        List<FestivalDTO> festivalDTOList = festivalService.selectAll();
        return new ResponseEntity<List<FestivalDTO>>(festivalDTOList, HttpStatus.OK);
    }
}

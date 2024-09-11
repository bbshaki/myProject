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
    public String calendar(Model model) {
        List<FestivalDTO> festivalDTOList = getFestivalList();
        model.addAttribute("festivalDTO", festivalDTOList);
        return "calendar";
    }

    // 축제 데이터를 JSON으로 반환하는 메서드
    @GetMapping("/calendar/aaa")
    public @ResponseBody ResponseEntity<List<FestivalDTO>> getCalendarData() {
        List<FestivalDTO> festivalDTOList = getFestivalList();
        return ResponseEntity.ok(festivalDTOList);
    }

    // 중복된 서비스 호출을 메서드로 추출하여 재사용
    private List<FestivalDTO> getFestivalList() {
        return festivalService.selectAll();
    }
}

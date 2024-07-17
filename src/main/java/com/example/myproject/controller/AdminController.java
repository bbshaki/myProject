package com.example.myproject.controller;

import com.example.myproject.dto.MemberUserDTO;
import com.example.myproject.dto.PageRequestDTO;
import com.example.myproject.dto.PageResponseDTO;
import com.example.myproject.service.AttractionService;
import com.example.myproject.service.FestivalService;
import com.example.myproject.service.MemberUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final FestivalService festivalService;
    private final AttractionService attractionService;
    private final MemberUserService memberUserService;

    @GetMapping("/memberList")
    public void memberList(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO<MemberUserDTO> pageResponseDTO = memberUserService.selectAll(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
    }

    @PostMapping("/attDelete/{ano}")
    public String deleteAtt(@PathVariable("ano") Long ano){
        log.info(ano);
        attractionService.delete(ano);
        return "redirect:/attraction/list";
    }

    @PostMapping("/fesDelete/{fno}")
    public String deleteFes(@PathVariable("fno") Long fno){
        festivalService.delete(fno);
        return "redirect:/festival/list";
    }

    @GetMapping("/memberRemove")
    public String removeMember(@RequestParam("mno") Long mno){
        memberUserService.memberRemove(mno);
        return "redirect:/members/list";
    }
}

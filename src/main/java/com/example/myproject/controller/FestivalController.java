package com.example.myproject.controller;

import com.example.myproject.constant.Category;
import com.example.myproject.constant.Progress;
import com.example.myproject.dto.*;
import com.example.myproject.entity.Festival;
import com.example.myproject.service.FestivalService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/festival/")
public class FestivalController {

    private final FestivalService festivalService;

    @GetMapping("/write")
    public String fesWrite(Model model){
        model.addAttribute("festivalDTO", new FestivalDTO());
        model.addAttribute("category", Category.values());
        model.addAttribute("progress", Progress.values());
        return "festival/write";
    }

    @PostMapping("/write")
    public String writeP(@Valid FestivalDTO festivalDTO, BindingResult bindingResult,
                         @RequestParam("fesImgFile") List<MultipartFile> fesImgFileList,
                         Model model, Principal principal){
        if (bindingResult.hasErrors()){
            return "/festival/write";
        }

        if (fesImgFileList.get(0).isEmpty() && festivalDTO.getFno() == null){
            model.addAttribute("errorMessage", "첫번째 이미지는 팔수 입력 값입니다");
        }
        try {
            festivalDTO.setWriter(principal.getName());
            festivalService.register(festivalDTO, fesImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "등록 중 에러가 발생했습니다.");
            return "/festival/write";
        }

        return "redirect:/festival/list";
    }

    @GetMapping({"/list", "/list/{page}"})
    public String fesList(FestivalSearchDTO festivalSearchDTO,
                          @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0 , 10);
        Page<FestivalDTO> festivals = festivalService.getPage(festivalSearchDTO, pageable);
        model.addAttribute("festivals", festivals);
        model.addAttribute("festivalSearchDTO", festivalSearchDTO);
        model.addAttribute("maxPage", 5);
        festivals.forEach(festival -> log.info(festival));
        return "/festival/list";
    }

    @GetMapping("/read")
    public String fesRead(Long fno, Model model, Principal principal){
        log.info(principal);
        try {
            FestivalDTO festivalDTO = festivalService.getDtl(fno);
            model.addAttribute("festivalDTO", festivalDTO);
        } catch (EntityNotFoundException e){
            model.addAttribute("errorMsg", "페이지가 없습니다");
            model.addAttribute("festivalDTO", new FestivalDTO());
        }
        return "festival/read";
    }

    @GetMapping("/modify")
    public void fesModify(Long fno, Model model, Principal principal){
        FestivalDTO festivalDTO = festivalService.getDtl(fno);
        model.addAttribute("festivalDTO", festivalDTO);
    }

    @PostMapping("/modify")
    public String attModify(FestivalDTO festivalDTO, @RequestParam("fesImgFile") List<MultipartFile> fesImgFileList, Model model){
        log.info("!!!!!!!!!!!!!!!!!!!!!!!" + festivalDTO);
        try {
            festivalService.updateFes(festivalDTO, fesImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMsg", "수정 중 에러가 발생했습니다");
            return "/festival/modify";
        }
        return "redirect:/festival/list";
    }

    @PostMapping("/delete/{fno}")
    public String delete(@PathVariable("fno") Long fno){
        log.info("여기까지 들어옴?");
        festivalService.delete(fno);
        return "redirect:/festival/list";
    }
}

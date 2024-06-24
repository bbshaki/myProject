package com.example.myproject.controller;

import com.example.myproject.dto.AttractionDTO;
import com.example.myproject.entity.Attraction;
import com.example.myproject.service.AttractionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/attraction/")
public class AttractionController {

    private final AttractionService attractionService;

    @GetMapping("/write")
    public void attG(){

    }

    @PostMapping("/write")
    public String attWrite(AttractionDTO attractionDTO, @RequestParam("attImgFile") List<MultipartFile> attImgFileList,
                           Model model, Principal principal){

        if (attImgFileList.get(0).isEmpty() && attractionDTO.getAno() == null){
            model.addAttribute("errorMsg", "첫번째 이미지는 팔수 입력 값입니다");
        }

        try {
            attractionDTO.setWriter(principal.getName());
            attractionService.register(attractionDTO, attImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "등록 중 에러가 발생했습니다.");
            return "/attraction/write";
        }

        return "redirect:/attraction/list";
    }

    @GetMapping("/list")
    public String attList(AttractionDTO attractionDTO, Model model){
        List<AttractionDTO> attractionList = attractionService.selectAll();
        log.info(attractionList);
        model.addAttribute("atrc", attractionList);
        return "attraction/list";
    }

    @GetMapping("/read")
    public void attRead(Long ano, Model model){
        log.info(ano);
        try {
            AttractionDTO attractionDTO = attractionService.getDetail(ano);
            log.info(attractionDTO.getFaImgDTOList());
            model.addAttribute("attractionDTO", attractionDTO);
        } catch (EntityNotFoundException e){
            model.addAttribute("errorMsg", "페이지가 없습니다");
            model.addAttribute("attractionDTO", new AttractionDTO());
        }
    }

    @GetMapping("/modify")
    public void attMod(Long ano, Model model){
        model.addAttribute("attractionDTO", attractionService.getDetail(ano));
        log.info("이 ano는 도대체 무엇인가" + ano);
    }

    @PostMapping("/modify")
    public String attModify(AttractionDTO attractionDTO, @RequestParam("attImgFile") List<MultipartFile> attImgFileList, Model model){
        log.info("!!!!!!!!!!!!!!!!!!!!!!!" + attractionDTO);
        try {
            attractionService.updateAtt(attractionDTO, attImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMsg", "수정 중 에러가 발생했습니다");
            return "/attraction/modify";
        }
        return "redirect:/attraction/list";
    }

    @PostMapping("/delete")
    public String delete(Long ano){
        log.info(ano);
        attractionService.delete(ano);
        return "redirect:/attraction/list";
    }

}



package com.example.myproject.controller;

import com.example.myproject.constant.Category;
import com.example.myproject.dto.AttractionDTO;
import com.example.myproject.dto.PageRequestDTO;
import com.example.myproject.dto.PageResponseDTO;
import com.example.myproject.entity.Attraction;
import com.example.myproject.service.AttractionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/attraction/")
public class AttractionController {

    private final AttractionService attractionService;

    @GetMapping("/write")
    public String attG(Model model){
        model.addAttribute("attractionDTO", new AttractionDTO());

        List<String> stringList = new ArrayList<>();

        for (Category a : Category.values()){
            stringList.add(a.getKrName());
        }

        model.addAttribute("categotys", stringList);
        return "/attraction/write";
    }

    @PostMapping("/write")
    public String attWrite(@Valid AttractionDTO attractionDTO, BindingResult bindingResult,
                           @RequestParam("attImgFile") List<MultipartFile> attImgFileList,
                           Model model, Principal principal){
        log.info("내용?"+ attractionDTO.getContent());
        log.info("타이틀?" + attractionDTO.getTitle());

        if (bindingResult.hasErrors()){
            log.info(bindingResult.hasErrors());
            return "/attraction/write";
        }

        if (attImgFileList.get(0).isEmpty() && attractionDTO.getAno() == null){
            model.addAttribute("errorMessage", "첫번째 이미지는 팔수 입력 값입니다");
            return "/attraction/write";
        }

        try {
            attractionDTO.setWriter(principal.getName());
            attractionService.register(attractionDTO, attImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/attraction/write";
        }

        return "redirect:/attraction/list";
    }

    @GetMapping("/list")
    public void attList(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO<AttractionDTO> pageResponseDTO = attractionService.list(pageRequestDTO);
        model.addAttribute("list", pageResponseDTO);
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
    public String attMod(Long ano, Model model, Principal principal){
        AttractionDTO attractionDTO = attractionService.getDetail(ano);
        log.info(attractionDTO.getWriter());
        if (!principal.getName().equals(attractionDTO.getWriter())){
            return "redirect:/attraction/read?ano=" + ano;
        }
        model.addAttribute("attractionDTO", attractionDTO);
        log.info("이 ano는 도대체 무엇인가" + ano);
        return "/attraction/modify";
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



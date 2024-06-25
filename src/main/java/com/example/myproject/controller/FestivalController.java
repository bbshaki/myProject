package com.example.myproject.controller;

import com.example.myproject.dto.AttractionDTO;
import com.example.myproject.dto.FestivalDTO;
import com.example.myproject.service.FestivalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/festival/")
public class FestivalController {

    private final FestivalService festivalService;

    @GetMapping("/write")
    public String fesWrite(){
        return "festival/write";
    }

    @PostMapping("/write")
    public String writeP(FestivalDTO festivalDTO, @RequestParam("fesImgFile") List<MultipartFile> fesImgFileList,
                         Model model, Principal principal){
        if (fesImgFileList.get(0).isEmpty() && festivalDTO.getFno() == null){
            model.addAttribute("errorMsg", "첫번째 이미지는 팔수 입력 값입니다");
        }
        try {
            festivalDTO.setWriter(principal.getName());
            festivalService.register(festivalDTO, fesImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMsg", "등록 중 에러가 발생했습니다.");
            return "/festival/write";
        }

        return "redirect:/festival/list";
    }

    @GetMapping("/list")
    public String fesList(FestivalDTO festivalDTO, Model model){
        List<FestivalDTO> festivalDTOList = festivalService.selectAll();
        model.addAttribute("fstv", festivalDTOList);
        return "festival/list";
    }

    @GetMapping("/read")
    public String fesRead(Long fno, Model model){
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
    public String fesModify(Long fno, Model model, Principal principal){
        FestivalDTO festivalDTO = festivalService.getDtl(fno);
        if (!principal.getName().equals(festivalDTO.getWriter())){
            return "redirect:/festival/read?fno=" + fno;
        }
        model.addAttribute("festivalDTO", festivalDTO);
        return "festival/modify";
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

    @PostMapping("/delete")
    public String delete(Long fno){
        festivalService.delete(fno);
        return "redirect:/festival/list";
    }
}

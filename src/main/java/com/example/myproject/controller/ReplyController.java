package com.example.myproject.controller;

import com.example.myproject.dto.PageRequestDTO;
import com.example.myproject.dto.PageResponseDTO;
import com.example.myproject.dto.ReplyDTO;
import com.example.myproject.entity.Reply;
import com.example.myproject.repository.ReplyRepository;
import com.example.myproject.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/replies/")
public class ReplyController {

    private final ReplyRepository replyRepository;
    private final ReplyService replyService;

    @PostMapping("/new")
    public Map<String, String> newReply(@RequestBody ReplyDTO replyDTO, Principal principal){
        replyDTO.setReplier(principal.getName());
        Long rno = replyService.register(replyDTO, principal);
        Map<String, String> result = new HashMap<>();
        result.put("rno", Long.toString(rno));
        return result;
    }

    @GetMapping(value = "/lists/{ano}")
    public PageResponseDTO<ReplyDTO> getListAtt(@PathVariable("ano") Long ano,
                                             PageRequestDTO pageRequestDTO){
        log.info("현재 본문은 : " + ano);
        log.info(pageRequestDTO);
        PageResponseDTO<ReplyDTO> pageResponseDTO =
                replyService.getListAtt(ano, pageRequestDTO);
        log.info(pageResponseDTO);
        return pageResponseDTO;
    }

    @GetMapping(value = "/list/{fno}")
    public PageResponseDTO<ReplyDTO> getListFes(@PathVariable("fno") Long fno,
                                                PageRequestDTO pageRequestDTO){
        log.info("현재 본문은 : " + fno);
        log.info(pageRequestDTO);
        PageResponseDTO<ReplyDTO> pageResponseDTO =
                replyService.getListFes(fno, pageRequestDTO);
        log.info(pageResponseDTO);
        return pageResponseDTO;
    }

    @GetMapping("/get/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno){

        return replyService.read(rno);
    }

    @PutMapping("/modify/{rno}")
    public Map<String, Long> modify(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO){
        replyDTO.setRno(rno);
        replyService.modify(replyDTO);
        Map<String, Long> map = new HashMap<>();
        map.put("rno", rno);

        return map;
    }

    @DeleteMapping("/remove/{rno}")
    public Map<String, Long> remove(@PathVariable("rno") Long rno){
        replyService.remove(rno);
        Map<String, Long> map = new HashMap<>();
        map.put("rno", rno);

        return map;
    }


}

package com.example.myproject.service;

import com.example.myproject.constant.Category;
import com.example.myproject.dto.AttractionDTO;
import com.example.myproject.dto.ReplyDTO;
import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.Reply;
import com.example.myproject.repository.AttractionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Log4j2
public class AttractionServiceTest {

    @Autowired
    private AttractionService attractionService;

    @Autowired
    private ReplyService replyService;

    @Test
    public void testAtt() throws Exception {
        List<MultipartFile> multipartFiles = new ArrayList<>();
        AttractionDTO attractionDTO = new AttractionDTO();
        attractionDTO.setAno(3L);
        attractionDTO.setTitle("타이틀이다");
        attractionDTO.setContent("콘텐츠입니다");
        attractionDTO.setCategory(Category.CHUNGBUK);
        attractionService.register(attractionDTO, multipartFiles);
        log.info(attractionDTO);

    }

    @Test
    public void testReply(){
        ReplyDTO replyDTO = ReplyDTO.builder()
                .ano(3L)
                .fno(null)
                .comment("cccccccc")
                .build();
        replyService.register(replyDTO);
        log.info(replyDTO);
    }
}

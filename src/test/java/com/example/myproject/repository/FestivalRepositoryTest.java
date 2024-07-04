package com.example.myproject.repository;

import com.example.myproject.constant.Category;
import com.example.myproject.constant.Progress;
import com.example.myproject.constant.Role;
import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.Festival;
import com.example.myproject.entity.MemberUser;
import com.example.myproject.entity.Reply;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class FestivalRepositoryTest {

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private MemberUserRepository memberUserRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private AttractionRepository attractionRepository;

    @Test
    @Transactional
    public void count(){
        LocalDate date = LocalDate.now();
        Long count = festivalRepository.countFestivalByScheduleS(date);
        log.info(count);
    }

    @Test
    @Transactional
    public void findTest(){
        List<Attraction> list = attractionRepository.findAll();
        log.info(list);
    }

}
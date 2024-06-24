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
    public void toTest(){
        Attraction attraction = new Attraction();
        attraction.setTitle("타이틀이다");
        attraction.setContent("콘텐츠다");
        attraction.setCategory(Category.DAEGU);
        attraction.setMemberUser(memberUserRepository.findById(1L).get());

        Reply reply = new Reply();
        reply.setAttraction(attractionRepository.save(attraction));
        reply.setMemberUser(memberUserRepository.findById(1L).get());
        reply.setComment("댓글 내용이다");

        replyRepository.save(reply);
        log.info(reply);

        reply.setComment("수정이다아아아아아아");
        attraction.setContent("이것도 수정이다!");
        log.info(replyRepository.findById(reply.getRno()));
        log.info(attractionRepository.findById(attraction.getAno()));



    }

    @Test
    public void create(){
        MemberUser memberUser = new MemberUser();
        memberUser.setEmail("a@a.com");
        memberUser.setId("idid");
        memberUser.setName("홍길동");
        memberUser.setRole(Role.USER);
        memberUser.setPassword("12341234");
        memberUser.setPhoneNumber("000000000");

        memberUser = memberUserRepository.findById(1L).get();

        Festival festival = Festival.builder()
                .category(Category.BUSAN)
                .title("타이틀이다")
                .content("콘텐츠 텍스트")
                .progress(Progress.EXPECT)
                .scheduleS(LocalDate.now())
                .scheduleF(LocalDate.now().plusDays(3))
                .memberUser(memberUser)
                .build();

        log.info(festivalRepository.save(festival));

        festival.setTitle("수정");
        festivalRepository.findById(festival.getFno());
        log.info(festival);
    }

    @Test
    @Transactional
    public void findTest(){
        List<Attraction> list = attractionRepository.findAll();
        log.info(list);
    }

}
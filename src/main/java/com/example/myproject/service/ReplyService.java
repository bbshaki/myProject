package com.example.myproject.service;

import com.example.myproject.dto.ReplyDTO;
import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.Festival;
import com.example.myproject.entity.MemberUser;
import com.example.myproject.entity.Reply;
import com.example.myproject.repository.AttractionRepository;
import com.example.myproject.repository.FestivalRepository;
import com.example.myproject.repository.MemberUserRepository;
import com.example.myproject.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;
    private final MemberUserRepository memberUserRepository;
    private final AttractionRepository attractionRepository;
    private final FestivalRepository festivalRepository;

    public Long register(ReplyDTO replyDTO){
        Reply reply = modelMapper.map(replyDTO, Reply.class);
        if (replyDTO.getAno() != null) {
            Attraction attraction = attractionRepository.findById(replyDTO.getAno()).get();
            reply.setAttraction(attraction);
        }
        if (replyDTO.getFno() != null){
            Festival festival = festivalRepository.findById(replyDTO.getFno()).get();
            reply.setFestival(festival);
        }


        return replyRepository.save(reply).getRno();
    }

    public List<Reply> replyList(Long rno){
        return replyRepository.findAll();
    }



}

package com.example.myproject.service;

import com.example.myproject.dto.PageRequestDTO;
import com.example.myproject.dto.PageResponseDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

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

    public Long register(ReplyDTO replyDTO, Principal principal){
        Reply reply = modelMapper.map(replyDTO, Reply.class);
        reply.setReplier(memberUserRepository.findMemberUserById(replyDTO.getReplier()).getId());
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

    public PageResponseDTO<ReplyDTO> getListAtt(Long ano, PageRequestDTO pageRequestDTO){
        Pageable pageable =
                PageRequest.of(pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() - 1,
                        pageRequestDTO.getSize() ,
                        Sort.by("rno").descending());
        Page<Reply> result = replyRepository.listOfAtt(ano, pageable);
        List<ReplyDTO> replyDTOList = result.getContent().stream()
                .map(reply -> modelMapper.map(reply, ReplyDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(replyDTOList)
                .total((int) result.getTotalElements()).build();
    }

    public PageResponseDTO<ReplyDTO> getListFes(Long fno, PageRequestDTO pageRequestDTO){
        Pageable pageable =
                PageRequest.of(pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() - 1,
                        pageRequestDTO.getSize() ,
                        Sort.by("rno").descending());
        Page<Reply> result = replyRepository.listOfFes(fno, pageable);
        List<ReplyDTO> replyDTOList = result.getContent().stream()
                .map(reply -> modelMapper.map(reply, ReplyDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(replyDTOList)
                .total((int) result.getTotalElements()).build();
    }

    public ReplyDTO read(Long rno){
        return modelMapper.map(replyRepository.findById(rno).get(), ReplyDTO.class);
    }

    public void modify(ReplyDTO replyDTO){
        Reply reply = replyRepository.findById(replyDTO.getRno()).get();
        reply.changeCom(replyDTO.getComment());
        replyRepository.save(reply);
    }

    public void remove(Long rno){
        replyRepository.deleteById(rno);
    }


}

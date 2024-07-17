package com.example.myproject.service;

import com.example.myproject.dto.*;
import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.FAImage;
import com.example.myproject.entity.Festival;
import com.example.myproject.repository.FestivalRepository;
import com.example.myproject.repository.ImgRepository;
import com.example.myproject.repository.MemberUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class FestivalService {

    private final FestivalRepository festivalRepository;
    private final FAImageService faImageService;
    private final ImgRepository imgRepository;
    private final MemberUserRepository memberUserRepository;
    private final FesSearchService fesSearchService;
    private final FesSearchCustom fesSearchCustom;
    private final ModelMapper modelMapper;

    public Long register(FestivalDTO festivalDTO, List<MultipartFile> multipartFiles) throws Exception{
        log.info("여기까지 들어오나??????????????????????");
        Festival festival = festivalDTO.createFes();
        festival.setWriter(memberUserRepository.findMemberUserById(festivalDTO.getWriter()).getId());
        festival.setRegTime(LocalDateTime.now());
        festivalRepository.save(festival);

        for (int i = 0; i < multipartFiles.size(); i++){
            FAImage faImage = new FAImage();
            faImage.setFestival(festival);

            if (i == 0){
                faImage.setRepImgYn("Y");
            } else {
                faImage.setRepImgYn("N");
            }
            faImageService.saveImg(faImage, multipartFiles.get(i));
        }
        return festival.getFno();
    }

    public List<FestivalDTO> selectAll(){
        List<Festival> festivalList = festivalRepository.findAll();
        List<FestivalDTO> festivalDTOList = new ArrayList<>();

        for (Festival festival : festivalList) {
            FestivalDTO festivalDTO = FestivalDTO.of(festival);
            festivalDTOList.add(festivalDTO);
        }
        return festivalDTOList;
    }

    public PageResponseDTO<FestivalDTO> list(PageRequestDTO pageRequestDTO){
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("fno");
        Page<Festival> festivalPage = fesSearchService.searchAll(types, keyword, pageable);
        List<FestivalDTO> festivalDTOList = festivalPage.getContent().stream()
                .map(festival -> modelMapper.map(festival, FestivalDTO.class)).collect(Collectors.toList());
        return PageResponseDTO.<FestivalDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(festivalDTOList)
                .total((int) festivalPage.getTotalElements())
                .build();
    }

    public Page<FestivalDTO> getPage(FestivalSearchDTO festivalSearchDTO, Pageable pageable){
        return fesSearchCustom.getPage(festivalSearchDTO, pageable);
    }

    public FestivalDTO getDtl(Long fno){
        List<FAImage> imgList = imgRepository.findImagesByFestivalFno(fno);
        List<FAImgDTO> faImgDTOList = new ArrayList<>();

        for (FAImage faImage : imgList){
            FAImgDTO faImgDTO = FAImgDTO.of(faImage);
            faImgDTOList.add(faImgDTO);
        }

        Festival festival = festivalRepository.findById(fno).orElseThrow(EntityNotFoundException::new);
        festival.setViewCount(festival.getViewCount() + 1);
        festivalRepository.save(festival);
        FestivalDTO festivalDTO = FestivalDTO.of(festival);
        festivalDTO.setFaImgDTOList(faImgDTOList);
        return festivalDTO;
    }

    public Long updateFes(FestivalDTO festivalDTO, List<MultipartFile> multipartFiles)throws Exception{
        Festival festival = festivalRepository.findById(festivalDTO.getFno())
                .orElseThrow(EntityNotFoundException::new);
        festival.updateFes(festivalDTO);

        List<Long> imgIds = festivalDTO.getImgIds();

        for (int i = 0; i < multipartFiles.size(); i++){
            faImageService.updateImg(imgIds.get(i), multipartFiles.get(i));
        }
        return festival.getFno();
    }

    public void delete(Long fno){
        Festival festival = festivalRepository.findById(fno).orElseThrow(EntityNotFoundException::new);
        festivalRepository.deleteById(festival.getFno());
    }


}

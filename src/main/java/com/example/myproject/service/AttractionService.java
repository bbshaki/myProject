package com.example.myproject.service;

import com.example.myproject.dto.*;
import com.example.myproject.entity.*;
import com.example.myproject.repository.AttractionRepository;
import com.example.myproject.repository.ImgRepository;
import com.example.myproject.repository.MemberUserRepository;
import com.example.myproject.repository.TodoRepository;
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
import java.util.Formatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class AttractionService {

    private final AttractionRepository attractionRepository;
    private final FAImageService faImageService;
    private final ImgRepository imgRepository;
    private final MemberUserRepository memberUserRepository;
    private final AttSearchService attSearchService;
    private final AttSearchCustom attSearchCustom;
    private final ModelMapper modelMapper;

    public Long register(AttractionDTO attractionDTO, List<MultipartFile> multipartFiles) throws Exception{
        Attraction attraction = attractionDTO.createAtt();
        attraction.setWriter(memberUserRepository.findMemberUserById(attractionDTO.getWriter()).getId());
        attraction.setRegTime(LocalDateTime.now());
        attractionRepository.save(attraction);

        for (int i = 0; i < multipartFiles.size(); i++){
            FAImage faImage = new FAImage();
            faImage.setAttraction(attraction);

            if (i == 0){
                faImage.setRepImgYn("Y");
            } else {
                faImage.setRepImgYn("N");
            }
            faImageService.saveImg(faImage, multipartFiles.get(i));
        }
        return attraction.getAno();
    }

    public List<AttractionDTO> selectAll(){
        List<Attraction> attractionList = attractionRepository.findAll();
        List<AttractionDTO> attractionDTOList = new ArrayList<>();

        for (Attraction attraction : attractionList) {
            AttractionDTO dto = AttractionDTO.of(attraction);
            attractionDTOList.add(dto);
        }

        return attractionDTOList;
    }

    public Page<AttractionDTO> getPage(AttractionSearchDTO attractionSearchDTO, Pageable pageable){
        return attSearchCustom.getPage(attractionSearchDTO, pageable);
    }

    public PageResponseDTO<AttractionDTO> list(PageRequestDTO pageRequestDTO){
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("ano");
        Page<Attraction> attractionPage = attSearchService.searchAll(types, keyword, pageable);
        List<AttractionDTO> attractionDTOList = attractionPage.getContent().stream()
                .map(attraction -> modelMapper.map(attraction, AttractionDTO.class)).collect(Collectors.toList());
        return PageResponseDTO.<AttractionDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(attractionDTOList)
                .total((int) attractionPage.getTotalElements())
                .build();
    }

    public AttractionDTO read(Long ano){
        Attraction attraction = attractionRepository.findById(ano).get();
        log.info(attraction);
        AttractionDTO attractionDTO = AttractionDTO.of(attraction);
        log.info(attractionDTO);
        return attractionDTO;
    }

    public AttractionDTO getDetail(Long ano){
        List<FAImage> imgList = imgRepository.findImagesByAttractionAno(ano);
        List<FAImgDTO> faImgDTOList = new ArrayList<>();

        for (FAImage faImage : imgList){
            FAImgDTO faImgDTO = FAImgDTO.of(faImage);
            faImgDTOList.add(faImgDTO);
        }
        Attraction attraction = attractionRepository.findById(ano).orElseThrow(EntityNotFoundException::new);
        attraction.setViewCount(attraction.getViewCount() + 1);
        attractionRepository.save(attraction);
        AttractionDTO attractionDTO = AttractionDTO.of(attraction);
        attractionDTO.setFaImgDTOList(faImgDTOList);
        return attractionDTO;
    }

    public Long updateAtt(AttractionDTO attractionDTO, List<MultipartFile> multipartFiles) throws Exception {
        Attraction attraction = attractionRepository.findById(attractionDTO.getAno())
                .orElseThrow(EntityNotFoundException::new);
        log.info("업데이트 대상은??????" + attraction);
        attraction.updateAtt(attractionDTO);

        List<Long> imgIds = attractionDTO.getImgIds();
        log.info(imgIds);

        for (int i = 0; i < multipartFiles.size(); i++){
            faImageService.updateImg(imgIds.get(i), multipartFiles.get(i));
        }
        log.info("이게 값이 있나??????" + attraction.getAno());
        return attraction.getAno();
    }

    public void delete(Long ano){
        Attraction attraction = attractionRepository.findById(ano).orElseThrow(EntityNotFoundException::new);
        attractionRepository.deleteById(attraction.getAno());
    }
}

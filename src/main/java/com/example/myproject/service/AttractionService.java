package com.example.myproject.service;

import com.example.myproject.dto.AttractionDTO;
import com.example.myproject.dto.FAImgDTO;
import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.FAImage;
import com.example.myproject.repository.AttractionRepository;
import com.example.myproject.repository.ImgRepository;
import com.example.myproject.repository.MemberUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class AttractionService {

    private final AttractionRepository attractionRepository;
    private final FAImageService faImageService;
    private final ImgRepository imgRepository;

    public Long register(AttractionDTO attractionDTO, List<MultipartFile> multipartFiles) throws Exception{
        Attraction attraction = attractionDTO.createAtt();
        attraction.setWriter("홍길동");
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

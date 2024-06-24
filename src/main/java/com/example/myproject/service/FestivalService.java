package com.example.myproject.service;

import com.example.myproject.dto.FAImgDTO;
import com.example.myproject.dto.FestivalDTO;
import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.FAImage;
import com.example.myproject.entity.Festival;
import com.example.myproject.repository.FestivalRepository;
import com.example.myproject.repository.ImgRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class FestivalService {

    private final FestivalRepository festivalRepository;
    private final FAImageService faImageService;
    private final ImgRepository imgRepository;

    public Long register(FestivalDTO festivalDTO, List<MultipartFile> multipartFiles) throws Exception{
        Festival festival = festivalDTO.createFes();
        festival.setWriter("홍길동");
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

    public FestivalDTO getDtl(Long fno){
        List<FAImage> imgList = imgRepository.findImagesByFestivalFno(fno);
        List<FAImgDTO> faImgDTOList = new ArrayList<>();

        for (FAImage faImage : imgList){
            FAImgDTO faImgDTO = FAImgDTO.of(faImage);
            faImgDTOList.add(faImgDTO);
        }

        Festival festival = festivalRepository.findById(fno).orElseThrow(EntityNotFoundException::new);
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

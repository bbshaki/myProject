package com.example.myproject.service;

import com.example.myproject.entity.FAImage;
import com.example.myproject.repository.ImgRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class FAImageService {

    @Value("${imgLocation}")
    private String imgLocation;

    private final ImgRepository imgRepository;

    private final FileService fileService;

    public void saveImg(FAImage faImage, MultipartFile multipartFile) throws Exception{
        log.info("여기에 들어옴???????????????????????????????????????");
        String originName = multipartFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if (!StringUtils.isEmpty(originName)){
            imgName = fileService.uploadFile(imgLocation, originName, multipartFile.getBytes());
            imgUrl = "/images/visit/" + imgName;
        }

        faImage.updateImg(originName, imgName, imgUrl);
        imgRepository.save(faImage);
        log.info(faImage.getImgUrl());
        log.info(faImage.getImgName());
        log.info(faImage.getOriginName());
    }

    public void updateImg(Long ino, MultipartFile multipartFile) throws Exception{
        if (!multipartFile.isEmpty()){
            FAImage saveImg = imgRepository.findById(ino).orElseThrow(EntityNotFoundException::new);
            if (!StringUtils.isEmpty(saveImg.getImgName())){
                fileService.deleteFile(imgLocation + "/" + saveImg.getImgName());
            }
            String originName = multipartFile.getOriginalFilename();
            String imgName = fileService.uploadFile(imgLocation, originName, multipartFile.getBytes());
            String imgUrl = "/images/visit/" + imgName;
            saveImg.updateImg(originName, imgName, imgUrl);
        }
    }
}

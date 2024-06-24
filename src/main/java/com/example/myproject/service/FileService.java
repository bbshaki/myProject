package com.example.myproject.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log4j2
public class FileService {
    
    public String uploadFile(String uploadPath, String originalFileName,
                             byte[] fileDate) throws Exception {

        UUID uuid = UUID.randomUUID();
        log.info("생성된 uuid : " + uuid);
        String extension = originalFileName.substring(originalFileName.indexOf("."));
        log.info("파일명 . 확장자 제외 : " + extension);
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        log.info(fileUploadFullUrl);

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileDate);
        fos.close();
        return savedFileName;
    }

    public void deleteFile (String filePath) {
        File deleteFile = new File(filePath);
        if (deleteFile.exists()){
            log.info("파일을 삭제 하였습니다.");
        }else {
            log.info("파일이 존재하지 않습니다.");
        }

    }


}

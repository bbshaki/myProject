package com.example.myproject.repository;

import com.example.myproject.entity.FAImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImgRepository extends JpaRepository<FAImage, Long> {

    @Query("select f from FAImage f where f.festival.fno = :fno")
    List<FAImage> findImagesByFestivalFno(Long fno);
    @Query("select f from FAImage f where f.attraction.ano = :ano")
    List<FAImage> findImagesByAttractionAno(Long ano);

    FAImage findByInoAndRepImgYn (Long ino, String repimgYn);


}

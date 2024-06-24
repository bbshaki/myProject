package com.example.myproject.dto;

import com.example.myproject.constant.Category;
import com.example.myproject.constant.Progress;
import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.Festival;
import com.example.myproject.entity.MemberUser;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FestivalDTO {

    private Long fno;

    private Progress progress; // 현재 진행 여부

    private String writer; // writer

    private Category category; // 지역 카테고리

    private String title;

    private String content;

    private LocalDate scheduleS;

    private LocalDate scheduleF;

    private int viewCount; // 조회수

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    private static ModelMapper modelMapper = new ModelMapper();

    public Festival createFes(){
        return modelMapper.map(this, Festival.class);
    }

    public static FestivalDTO of(Festival festival){
        return modelMapper.map(festival, FestivalDTO.class);
    }

    private List<FAImgDTO> faImgDTOList = new ArrayList<>();

    private List<Long> imgIds = new ArrayList<>();

}

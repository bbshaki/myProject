package com.example.myproject.dto;

import com.example.myproject.constant.Category;
import com.example.myproject.constant.Progress;
import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.Festival;
import com.example.myproject.entity.MemberUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "제목은 필수로 입력해 주세요")
    private String title;

    @NotBlank(message = "내용은 필수로 입력해 주세요")
    private String content;

    private LocalDate scheduleS;

    private LocalDate scheduleF;

    private int viewCount; // 조회수

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    private static ModelMapper modelMapper = new ModelMapper();

    public Festival createFes(){
        Festival festival = modelMapper.map(this, Festival.class);

        return festival;
    }

    public static FestivalDTO of(Festival festival){
        return modelMapper.map(festival, FestivalDTO.class);
    }

    private List<FAImgDTO> faImgDTOList = new ArrayList<>();

    private List<Long> imgIds = new ArrayList<>();


}

package com.example.myproject.dto;

import com.example.myproject.constant.Category;
import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.MemberUser;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttractionDTO {

    private Long ano;

    private String writer; // writer

    private Category category;

    private String title;

    private String content;

    private int viewCount; // 조회수

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    private static ModelMapper modelMapper = new ModelMapper();

    public Attraction createAtt(){
        return modelMapper.map(this, Attraction.class);
    }

    public static AttractionDTO of(Attraction attraction){
        return modelMapper.map(attraction, AttractionDTO.class);
    }

    private List<FAImgDTO> faImgDTOList = new ArrayList<>();

    private List<Long> imgIds = new ArrayList<>();
}

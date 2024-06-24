package com.example.myproject.dto;

import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.FAImage;
import com.example.myproject.entity.Festival;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FAImgDTO {

    private Long ino;

    private Festival fno;

    private Attraction ano;

    private String imgName;

    private String originName;

    private String imgUrl;

    private String repImgYn; // 대표 이미지 여부

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    private static ModelMapper modelMapper = new ModelMapper();

    public static FAImgDTO of(FAImage faImage){
        return modelMapper.map(faImage, FAImgDTO.class);
    }
}

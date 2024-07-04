package com.example.myproject.dto;

import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.Festival;
import com.example.myproject.entity.MemberUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDTO {

    private Long rno;

    private Long fno;

    private Long ano;

    @NotEmpty(message = "댓글 내용은 필수로 입력해 주세요")
    private String comment;

    private String replier; // 작성자

    private LocalDateTime regTime;

    private LocalDateTime updateTime;



}

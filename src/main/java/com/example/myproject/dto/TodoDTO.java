package com.example.myproject.dto;

import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.Festival;
import com.example.myproject.entity.MemberUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO {

    private Long tno;

    private MemberUser memberUser;

    private Long fno;

    private Long ano;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}

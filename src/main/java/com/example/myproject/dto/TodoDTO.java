package com.example.myproject.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO {

    private Long tno;

    private Long mno;

    private String title;

    private Long fno;

    private Long ano;

}

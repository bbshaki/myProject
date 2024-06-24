package com.example.myproject.dto;

import com.example.myproject.constant.Role;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUserDTO {

    private Long mno;

    private String id;

    private String name;

    private String password;

    private String email;

    private String phoneNumber;

    private Role role;
}

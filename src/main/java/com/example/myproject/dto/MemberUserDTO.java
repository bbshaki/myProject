package com.example.myproject.dto;

import com.example.myproject.constant.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUserDTO {

    private Long mno;

    @NotBlank(message = "아이디는 필수 입력 값 입니다.")
    private String id;

    @NotBlank(message = "이름은 필수 입력 값 입니다.")
    private String name;

    @NotBlank(message = "비밀번호는 필수 입력 값 입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotEmpty(message = "휴대 전화 번호를 올바르게 입력해 주세요(숫자만 입력 가능)")
    private String phoneNumber;
}

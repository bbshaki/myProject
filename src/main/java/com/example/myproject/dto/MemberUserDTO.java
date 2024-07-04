package com.example.myproject.dto;

import com.example.myproject.constant.Role;
import com.example.myproject.entity.MemberUser;
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

    @NotBlank(message = "아이디는 필수로 입력해 주세요")
    private String id;

    @NotBlank(message = "이름은 필수로 입력해 주세요")
    private String name;

    @NotBlank(message = "비밀번호는 필수로 입력해 주세요")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "비밀번호는 8~16자 사이 영문 대/소문자와 숫자, 특수문자를 반드시 사용해야 합니다")
    private String password;

    private String secPassword;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "비밀번호는 8~16자 사이 영문 대/소문자와 숫자, 특수문자를 반드시 사용해야 합니다")
    private String newPassword;

    @NotBlank(message = "이메일은 필수로 입력해 주세요")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotEmpty(message = "휴대전화번호를 올바르게 입력해 주세요('-' 제외 숫자만)")
    private String phoneNumber;

    public MemberUserDTO(MemberUser memberUser) {
        this.mno = memberUser.getMno();
        this.id = memberUser.getId();
        this.email = memberUser.getEmail();
    }
}

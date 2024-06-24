package com.example.myproject.entity;

import com.example.myproject.constant.Role;
import com.example.myproject.dto.MemberUserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "memberUser")
@Getter
@Setter
@ToString
public class MemberUser {

    @Id
    @Column(name = "mno")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(name = "id", unique = true)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static MemberUser createMember(MemberUserDTO memberUserDTO, PasswordEncoder passwordEncoder){
        MemberUser memberUser = new MemberUser();
        memberUser.setEmail(memberUserDTO.getEmail());
        memberUser.setName(memberUserDTO.getName());
        memberUser.setId(memberUserDTO.getId());
        memberUser.setPhoneNumber(memberUserDTO.getPhoneNumber());

        String password = passwordEncoder.encode(memberUserDTO.getPassword());
        memberUser.setPassword(password);

        memberUser.setRole(Role.USER);

        return memberUser;
    }

}

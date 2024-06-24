package com.example.myproject.entity;

import com.example.myproject.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

}

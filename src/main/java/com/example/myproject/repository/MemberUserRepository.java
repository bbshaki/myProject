package com.example.myproject.repository;

import com.example.myproject.entity.Festival;
import com.example.myproject.entity.MemberUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberUserRepository extends JpaRepository<MemberUser, Long> {

    MemberUser findByEmail(String email);

    MemberUser findByName(String name);
}

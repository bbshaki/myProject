package com.example.myproject.repository;

import com.example.myproject.entity.Festival;
import com.example.myproject.entity.MemberUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberUserRepository extends JpaRepository<MemberUser, Long> {

    MemberUser findMemberUserById(String id);

    MemberUser findMemberUserByEmailAndName(String email, String name);

    MemberUser findMemberUserByIdAndEmail(String id, String email);
}

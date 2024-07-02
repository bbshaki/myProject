package com.example.myproject.repository;

import com.example.myproject.entity.MemberUser;
import com.example.myproject.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByMemberUser(MemberUser memberUser);
}

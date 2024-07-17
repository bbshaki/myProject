package com.example.myproject.repository;

import com.example.myproject.entity.Festival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface FestivalRepository extends JpaRepository<Festival, Long>, QuerydslPredicateExecutor<Festival> {

    Long countFestivalByScheduleS(LocalDate date);

}

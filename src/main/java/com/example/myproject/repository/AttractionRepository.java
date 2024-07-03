package com.example.myproject.repository;

import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.Festival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttractionRepository extends JpaRepository<Attraction, Long> {

    @Query("update Attraction a set a.viewCount = a.viewCount + 1 where a.ano = :ano")
    void updateCount(Long ano);

}

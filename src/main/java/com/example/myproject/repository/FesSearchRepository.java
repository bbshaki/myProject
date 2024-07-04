package com.example.myproject.repository;

import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FesSearchRepository extends JpaRepository<Festival, Long> {

}

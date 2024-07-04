package com.example.myproject.repository;

import com.example.myproject.entity.Attraction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AttSearchRepository extends JpaRepository<Attraction, Long> {

}

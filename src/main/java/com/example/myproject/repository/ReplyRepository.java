package com.example.myproject.repository;

import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.Festival;
import com.example.myproject.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Query("select r from Reply r where r.attraction.ano = :ano")
    List<Reply> listReplyFromAno(Long ano);

    @Query("select r from Reply r where r.festival.fno = :fno")
    List<Reply> listReplyFromFno(Long fno);

    List<Reply> findByAttraction(Attraction attraction);
    List<Reply> findByFestival(Festival festival);

    @Query("select r from Reply r where r.attraction.ano = :ano")
    Page<Reply> listOfAtt(Long ano, Pageable pageable);
    @Query("select r from Reply r where r.festival.fno = :fno")
    Page<Reply> listOfFes(Long fno, Pageable pageable);


}

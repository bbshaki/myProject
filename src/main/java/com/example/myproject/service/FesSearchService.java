package com.example.myproject.service;

import com.example.myproject.dto.FestivalSearchDTO;
import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.Festival;
import com.example.myproject.entity.QAttraction;
import com.example.myproject.entity.QFestival;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
@Transactional
public class FesSearchService extends QuerydslRepositorySupport {
    public FesSearchService(){
        super(Festival.class);
    }

    public Page<Festival> jpqlQuerygetPage(FestivalSearchDTO festivalSearchDTO, Pageable pageable) {

        QFestival festival = QFestival.festival;
        JPQLQuery<Festival> query = from(festival);

        BooleanBuilder booleanBuilder =new BooleanBuilder();
        LocalDateTime localDateTime = LocalDateTime.now();// 현재

        if(festivalSearchDTO.getCategory() != null){
            booleanBuilder.and(festival.category.eq(festivalSearchDTO.getCategory()));
        }

        if(festivalSearchDTO.getProgress() != null){
            booleanBuilder.and(festival.progress.eq(festivalSearchDTO.getProgress()));
        }

        if(StringUtils.equals("all", festivalSearchDTO.getSearchDate())  || festivalSearchDTO.getSearchDate() == null){

        }
        if(StringUtils.equals("title", festivalSearchDTO.getSearchBy())){
            booleanBuilder.and(festival.title.like("%" +festivalSearchDTO.getSearchQuery()+ "%"));

        }else if(StringUtils.equals("content",  festivalSearchDTO.getSearchBy()) ){

            booleanBuilder.and(festival.content.like("%" +festivalSearchDTO.getSearchQuery()+ "%"));

        }

        query.where(booleanBuilder);

        query.where(festival.fno.gt(0L));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Festival> content = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(content, pageable, count);
    }

    public Page<Festival> searchAll(String[] types, String keyword, Pageable pageable) {

        QFestival festival = QFestival.festival;

        JPQLQuery<Festival> query = from(festival);

        if( (types != null  && types.length >0) && keyword !=null   ){

            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String string : types){

                switch (string){
                    case "t":
                        booleanBuilder.or(festival.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(festival.content.contains(keyword));
                        break;
                    case "ca":
                    booleanBuilder.or(festival.category.stringValue().contains(keyword));
                        break;
                    case "p":
                    booleanBuilder.or(festival.progress.stringValue().contains(keyword));
                        break;
                }

            }
            query.where(booleanBuilder);
        }

        query.where(festival.fno.gt(0L));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Festival> festivalList = query.fetch();

        long count = query.fetchCount();

        return new PageImpl<>(festivalList, pageable, count);
    }



}


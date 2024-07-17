package com.example.myproject.service;

import com.example.myproject.dto.AttractionSearchDTO;
import com.example.myproject.entity.Attraction;
import com.example.myproject.entity.QAttraction;
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
public class AttSearchService extends QuerydslRepositorySupport {
    public AttSearchService(){
        super(Attraction.class);
    }

    public Page<Attraction> jpqlQuerygetPage(AttractionSearchDTO attractionSearchDTO, Pageable pageable) {

        QAttraction attraction = QAttraction.attraction;
        JPQLQuery<Attraction> query = from(attraction);

        BooleanBuilder booleanBuilder =new BooleanBuilder();
        LocalDateTime localDateTime = LocalDateTime.now();

        if(attractionSearchDTO.getCategory() != null){
            booleanBuilder.and(attraction.category.eq(attractionSearchDTO.getCategory()));
        }

        if(StringUtils.equals("all", attractionSearchDTO.getSearchDate())  || attractionSearchDTO.getSearchDate() == null){

        }

        if(StringUtils.equals("title", attractionSearchDTO.getSearchBy())){
            booleanBuilder.and(attraction.title.like("%" +attractionSearchDTO.getSearchQuery()+ "%"));

        }else if(StringUtils.equals("content",  attractionSearchDTO.getSearchBy()) ){

            booleanBuilder.and(attraction.content.like("%" +attractionSearchDTO.getSearchQuery()+ "%"));

        }

        query.where(booleanBuilder);

        query.where(attraction.ano.gt(0L));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Attraction> content = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(content, pageable, count);
    }

    public Page<Attraction> searchAll(String[] types, String keyword, Pageable pageable) {

        QAttraction attraction = QAttraction.attraction;

        JPQLQuery<Attraction> query = from(attraction);

        if( (types != null  && types.length >0) && keyword !=null   ){

            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String string : types){

                switch (string){
                    case "t":
                        booleanBuilder.or(attraction.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(attraction.content.contains(keyword));
                        break;
                    case "ca":
                    booleanBuilder.or(attraction.category.stringValue().contains(keyword));
                        break;
                }

            }
            query.where(booleanBuilder);
        }

        query.where(attraction.ano.gt(0L));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Attraction> attractionList = query.fetch();

        long count = query.fetchCount();

        return new PageImpl<>(attractionList, pageable, count);
    }



}


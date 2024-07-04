package com.example.myproject.service;

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

import java.util.List;

@Service
@Log4j2
@Transactional
public class AttSearchService extends QuerydslRepositorySupport {
    public AttSearchService(){
        super(Attraction.class);
    }

    public Page<Attraction> searchAtt(Pageable pageable){
        QAttraction attraction = QAttraction.attraction;
        JPQLQuery<Attraction> query = from(attraction);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.or(attraction.title.contains("1"));
        booleanBuilder.or(attraction.content.contains("1"));
        booleanBuilder.or(attraction.category.stringValue().contains("1"));

        query.where(booleanBuilder);
        query.where(attraction.ano.gt(0L));
        this.getQuerydsl().applyPagination(pageable, query);

        List<Attraction> list = query.fetch();
        Long count = query.fetchCount();

        return null;
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

        List<Attraction> boardList = query.fetch();

        long count = query.fetchCount();

        return new PageImpl<>(boardList, pageable, count);
    }



}


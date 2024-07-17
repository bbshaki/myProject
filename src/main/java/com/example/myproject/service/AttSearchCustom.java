package com.example.myproject.service;

import com.example.myproject.constant.Category;
import com.example.myproject.dto.AttractionDTO;
import com.example.myproject.dto.AttractionSearchDTO;
import com.example.myproject.dto.MainDTO;
import com.example.myproject.dto.QMainDTO;
import com.example.myproject.entity.*;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttSearchCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    private ModelMapper modelMapper;

    public AttSearchCustom(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    public BooleanExpression titleLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QAttraction.attraction.title.like("%" + searchQuery + "%");
    }

    private BooleanExpression searchCategory(Category category){
        return category == null ? null : QAttraction.attraction.category.eq(category);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("title",  searchBy) ){
            return QAttraction.attraction.title.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("content",  searchBy) ){
            return QAttraction.attraction.content.like("%" + searchQuery + "%");
        }
        return null;
    }

    public Page<AttractionDTO> getPage(AttractionSearchDTO attractionSearchDTO, Pageable pageable) {
        QueryResults<Attraction> result =  jpaQueryFactory.selectFrom(QAttraction.attraction)
                .where(searchCategory(attractionSearchDTO.getCategory()),
                        searchByLike( attractionSearchDTO.getSearchBy(), attractionSearchDTO.getSearchQuery() )
                )
                .orderBy(QAttraction.attraction.ano.desc() )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Attraction> content = result.getResults();

        List<AttractionDTO> attractionDTOList = content.stream().map(attraction ->
                modelMapper.map(attraction, AttractionDTO.class)).collect(Collectors.toList());

        long total = result.getTotal();
        return new PageImpl<>(attractionDTOList, pageable, total);
    }

    public Page<MainDTO> getMainItemPage(AttractionSearchDTO attractionSearchDTO, Pageable pageable) {
        QFestival festival = QFestival.festival;
        QFAImage image = QFAImage.fAImage;
        QAttraction attraction = QAttraction.attraction;

        QueryResults<MainDTO> result =  jpaQueryFactory.select(
                        new QMainDTO(
                                attraction.ano,
                                festival.fno,
                                attraction.title,
                                attraction.content,
                                image.imgUrl
                        )
                )
                .from(image)
                .join(image.attraction, attraction)
                .where(image.repImgYn.eq("Y"))    //대표이미지
                .where(titleLike(attractionSearchDTO.getSearchQuery()))  //상품명검색 입력받은것과 같은거
                .orderBy(attraction.ano.desc() )
                .offset(pageable.getOffset())           // 몇번부터 1번글부터 // 11번글 부터
                .limit(pageable.getPageSize())          //size = 10 10개씩
                .fetchResults();

        List<MainDTO> content = result.getResults();
        long total = result.getTotal();


        return new PageImpl<>(content, pageable, total);


    }
}

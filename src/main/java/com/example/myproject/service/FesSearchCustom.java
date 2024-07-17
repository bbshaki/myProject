package com.example.myproject.service;

import com.example.myproject.constant.Category;
import com.example.myproject.constant.Progress;
import com.example.myproject.dto.FestivalDTO;
import com.example.myproject.dto.FestivalSearchDTO;
import com.example.myproject.dto.MainDTO;
import com.example.myproject.dto.QMainDTO;
import com.example.myproject.entity.Festival;
import com.example.myproject.entity.QAttraction;
import com.example.myproject.entity.QFAImage;
import com.example.myproject.entity.QFestival;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class FesSearchCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    private ModelMapper modelMapper;




    public FesSearchCustom(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    public BooleanExpression titleLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QFestival.festival.title.like("%" + searchQuery + "%");
    }

    private BooleanExpression searchCategory(Category category){
        return category == null ? null : QFestival.festival.category.eq(category);
    }

    private BooleanExpression searchProgress(Progress progress){
        return progress == null ? null : QFestival.festival.progress.eq(progress);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("title",  searchBy) ){
            return QFestival.festival.title.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("content",  searchBy) ){
            return QFestival.festival.content.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("title&content", searchBy)) {
            return QFestival.festival.title.like("%" + searchQuery + "%")
                    .or(QFestival.festival.content.like("%" + searchQuery + "%"));
        }
        return null;
    }

    public Page<FestivalDTO> getPage(FestivalSearchDTO festivalSearchDTO, Pageable pageable) {
        QueryResults<Festival> result =  jpaQueryFactory.selectFrom(QFestival.festival)
                .where(searchCategory(festivalSearchDTO.getCategory()),
                        searchProgress(festivalSearchDTO.getProgress()),
                        searchByLike( festivalSearchDTO.getSearchBy(), festivalSearchDTO.getSearchQuery() )
                )
                .orderBy(QFestival.festival.fno.desc() )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Festival> content = result.getResults();

        List<FestivalDTO> festivalDTOList
                = content.stream().map(festival -> modelMapper.map(festival, FestivalDTO.class))
                .collect(Collectors.toList());


        long total = result.getTotal();
        return new PageImpl<>(festivalDTOList, pageable, total);
    }

    public Page<MainDTO> getMainItemPage(FestivalSearchDTO festivalSearchDTO, Pageable pageable) {
        QFestival festival = QFestival.festival;
        QFAImage image = QFAImage.fAImage;
        QAttraction attraction = QAttraction.attraction;

        QueryResults<MainDTO> result =  jpaQueryFactory.select(
                        new QMainDTO(
                                attraction.ano,
                                festival.fno,
                                festival.title,
                                festival.content,
                                image.imgUrl
                        )
                )
                .from(image)
                .join(image.festival, festival)
                .where(image.repImgYn.eq("Y"))    //대표이미지
                .where(titleLike(festivalSearchDTO.getSearchQuery()))  //상품명검색 입력받은것과 같은거
                .orderBy(festival.fno.desc() )
                .offset(pageable.getOffset())           // 몇번부터 1번글부터 // 11번글 부터
                .limit(pageable.getPageSize())          //size = 10 10개씩
                .fetchResults();

        List<MainDTO> content = result.getResults();
        long total = result.getTotal();


        return new PageImpl<>(content, pageable, total);


    }
}

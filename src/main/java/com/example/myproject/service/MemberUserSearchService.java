package com.example.myproject.service;

import com.example.myproject.entity.MemberUser;
import com.example.myproject.entity.QMemberUser;
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

@Log4j2
@Transactional
@Service
public class MemberUserSearchService extends QuerydslRepositorySupport {

    public MemberUserSearchService(){
        super(MemberUser.class);
    }

    public Page<MemberUser> searchAll(String[] types, String keyword, Pageable pageable) {

        QMemberUser memberUser = QMemberUser.memberUser; //q도메인 객체

        JPQLQuery<MemberUser> query = from(memberUser);

        if( (types != null  && types.length >0) && keyword !=null   ){

            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String string : types){

                switch (string){
                    case "i":
                        booleanBuilder.or(memberUser.id.contains(keyword));
                        break;
                    case "n":
                        booleanBuilder.or(memberUser.name.contains(keyword));
                        break;
                    case "e":
                        booleanBuilder.or(memberUser.email.contains(keyword));
                        break;
                }//switch

            }//for
            query.where(booleanBuilder);

        }//if

        query.where(memberUser.mno.gt(0L));
        System.out.println("0보다 큰조건  mno가 " + query);

        //페이징
        this.getQuerydsl().applyPagination(pageable, query);


        List<MemberUser> memberUserList = query.fetch(); //실행
        memberUserList.forEach(memberUser1 -> log.info(memberUser1));
        long count = query.fetchCount(); //row수

        System.out.println(count);



        return new PageImpl<>(memberUserList, pageable, count);
    }
}

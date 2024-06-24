package com.example.myproject.service;

import com.example.myproject.constant.Role;
import com.example.myproject.entity.MemberUser;
import com.example.myproject.repository.MemberUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class MemberUserService implements UserDetailsService {

    private final MemberUserRepository memberUserRepository;

    public MemberUser saveMember(MemberUser memberUser){
        validateDuplicateMember(memberUser);
        return memberUserRepository.save(memberUser);
    }

    private void validateDuplicateMember(MemberUser memberuser) {
        log.info("이미 가입된 회원인지 확인");
        // 데이터베이스에 저장된 회원가입이 되어있는지 찾아본다.
        MemberUser findMember = memberUserRepository.findMemberUserById(memberuser.getId());

        //이미 가입된 email이라면
        if(findMember != null) {
            log.info("이미 가입된 회원");
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        MemberUser memberuser = this.memberUserRepository.findMemberUserById(id);

        if(memberuser == null){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다." + id);
        }
//        User user1 = user.get();  optional 이라서 했던 코드
//        List<GrantedAuthority> authorities = new ArrayList<>(); // 권한들
        log.info(memberuser);
        log.info("현재 로그인하신분의 권한 : " +memberuser.getRole().name());
        String role  = "";
        if("ADMIN".equals(memberuser.getRole().name())){   //auth 컬럼을 추가로 지정해서 사용
            log.info("관리자");
            role = Role.ADMIN.name();
//            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.name()));
        }else {
            log.info("유저");
            role = Role.USER.name();
            // authorities.add(new SimpleGrantedAuthority(Role.USER.name()));
        }


        return User.builder()
                .username( memberuser.getId() )
                .password( memberuser.getPassword())
                .roles(role)
                .build();
    }
}

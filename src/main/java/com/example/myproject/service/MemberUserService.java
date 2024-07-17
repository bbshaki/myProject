package com.example.myproject.service;

import com.example.myproject.constant.Role;
import com.example.myproject.dto.MemberUserDTO;
import com.example.myproject.dto.PageRequestDTO;
import com.example.myproject.dto.PageResponseDTO;
import com.example.myproject.entity.MemberUser;
import com.example.myproject.repository.MemberUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class MemberUserService implements UserDetailsService {

    private final MemberUserRepository memberUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final MemberUserSearchService memberUserSearchService;

    public MemberUser saveMember(MemberUser memberUser){
        validateDuplicateMember(memberUser);
        return memberUserRepository.save(memberUser);
    }

    private void validateDuplicateMember(MemberUser memberuser) {
        MemberUser findMember = memberUserRepository.findMemberUserById(memberuser.getId());
        if(findMember != null) {
            log.info("이미 가입된 회원");
            throw new IllegalStateException("이미 가입된 회원입니다. 아이디(ID) / 이메일(E-mail) 중복 가입 불가");
        }

    }

    public PageResponseDTO<MemberUserDTO> selectAll(PageRequestDTO pageRequestDTO){
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("mno");
        Page<MemberUser> memberUserPage = memberUserSearchService.searchAll(types, keyword, pageable);
        List<MemberUserDTO> memberUserDTOList = memberUserPage.getContent().stream().map(memberUser ->
                modelMapper.map(memberUser, MemberUserDTO.class)).collect(Collectors.toList());
        return PageResponseDTO.<MemberUserDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(memberUserDTOList)
                .total((int)memberUserPage.getTotalElements())
                .build();
    }

    public MemberUserDTO read(String id){
        MemberUser memberUser = memberUserRepository.findMemberUserById(id);
        if (memberUser == null){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
        }
        MemberUserDTO memberUserDTO = modelMapper.map(memberUser, MemberUserDTO.class);
        return memberUserDTO;
    }

    public void newPassword(MemberUserDTO memberUserDTO, String newPassword){
        MemberUser memberUser = memberUserRepository.findMemberUserById(memberUserDTO.getId());
        memberUser.setPassword(passwordEncoder.encode(newPassword));
        memberUser.setRole(Role.USER);
    }

    public void savePw(String id, String pw){
        log.info(id);
        log.info(pw);
        MemberUser memberUser = memberUserRepository.findMemberUserById(id);
        memberUser.setPassword(passwordEncoder.encode(pw));
    }

    public MemberUserDTO findID(String email, String name){
        MemberUser memberUser = memberUserRepository.findMemberUserByEmailAndName(email, name);
        if (memberUser != null){
            return new MemberUserDTO(memberUser);
        } else {
            return null;
        }
    }

    public MemberUserDTO emailCheck(String id, String email){
        MemberUser memberUser = memberUserRepository.findMemberUserByIdAndEmail(id, email);
        log.info(memberUser);
        if (memberUser != null && memberUser.getId().equals(id) && memberUser.getEmail().equals(email)){
            MemberUserDTO memberUserDTO = modelMapper.map(memberUser, MemberUserDTO.class);
            return memberUserDTO;
        } else {
            return null;
        }
    }

    public void memberRemove(Long mno){
        log.info("or here???????????????????????");
        MemberUser memberUser = memberUserRepository.findById(mno).get();
        memberUserRepository.deleteById(memberUser.getMno());
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        MemberUser memberuser = this.memberUserRepository.findMemberUserById(id);

        if(memberuser == null){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다." + id);
        }
        String role  = "";
        if("ADMIN".equals(memberuser.getRole().name())){
            role = Role.ADMIN.name();
        }else {
            log.info("유저");
            role = Role.USER.name();
        }
        return User.builder()
                .username( memberuser.getId() )
                .password( memberuser.getPassword())
                .roles(role)
                .build();
    }
}

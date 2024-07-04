package com.example.myproject.service;

import com.example.myproject.constant.Role;
import com.example.myproject.dto.MemberUserDTO;
import com.example.myproject.entity.MemberUser;
import com.example.myproject.repository.MemberUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class MemberUserService implements UserDetailsService {

    private final MemberUserRepository memberUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public MemberUser saveMember(MemberUser memberUser){
        validateDuplicateMember(memberUser);
        return memberUserRepository.save(memberUser);
    }

    private void validateDuplicateMember(MemberUser memberuser) {
        MemberUser findMember = memberUserRepository.findMemberUserById(memberuser.getId());
        if(findMember != null) {
            log.info("이미 가입된 회원");
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

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
        memberUserDTO.setPassword(passwordEncoder.encode(newPassword));
        MemberUser memberUser = modelMapper.map(memberUserDTO, MemberUser.class);
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

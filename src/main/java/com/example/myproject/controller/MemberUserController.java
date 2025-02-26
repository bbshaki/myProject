package com.example.myproject.controller;

import com.example.myproject.dto.MemberUserDTO;
import com.example.myproject.dto.TodoDTO;
import com.example.myproject.entity.MemberUser;
import com.example.myproject.entity.Todo;
import com.example.myproject.service.MemberUserService;
import com.example.myproject.service.TodoService;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/members/")
public class MemberUserController {

    private final PasswordEncoder passwordEncoder;
    private final MemberUserService memberUserService;
    private final TodoService todoService;
    private final JavaMailSender mailSender;

    @GetMapping("/register")
    public String memberRegister(Model model){
        model.addAttribute("memberUserDTO", new MemberUserDTO());
        return "/members/register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid MemberUserDTO memberUserDTO, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "/members/register";
        }

        if (!memberUserDTO.getPassword().equals(memberUserDTO.getSecPassword())){
            bindingResult.rejectValue("secPassword", "passwordInCorrect", "비밀번호가 일치하지 않습니다");
            return "/members/register";
        }

        try {
            MemberUser memberUser = MemberUser.createMember(memberUserDTO, passwordEncoder);
            memberUserService.saveMember(memberUser);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "/members/register";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){

        return "/members/login";
    }

    @GetMapping("/login/error")
    public String loginError(Model model){

        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");

        return "/members/login";
    }

    @GetMapping("/findID")
    public void findId(){
    }

    @PostMapping("/findID")
    public String findIdPost(Model model, String email, String name){
        MemberUserDTO memberUserDTO = memberUserService.findID(email, name);
        if (memberUserDTO != null){
            model.addAttribute("id", memberUserDTO.getId());
            return "/members/showId";
        } else {
            model.addAttribute("errorMessage", "사용자를 찾을 수 없습니다");
            return "/members/findID";
        }
    }

    @GetMapping({"/read", "/modify"})
    public void read(Principal principal, Model model){
        model.addAttribute("memberUserDTO", memberUserService.read(principal.getName()));
    }

    @GetMapping("/todoList")
    public void todoList(Model model, Principal principal){
        List<TodoDTO> tododtoList = todoService.todoDTOList(principal);
        model.addAttribute("todoList", tododtoList);
    }

    @PostMapping("/modify")
    public String memberModify(Principal principal, String password, String newPassword, Model model){
        log.info(password);
        log.info(newPassword);
        MemberUserDTO memberUserDTO = memberUserService.read(principal.getName());
        model.addAttribute("memberUserDTO", memberUserDTO);
        log.info(memberUserDTO.getPassword());
        if (passwordEncoder.matches(password, memberUserDTO.getPassword())){
            memberUserService.newPassword(memberUserDTO, newPassword);
            log.info(memberUserDTO.getPassword());
            model.addAttribute("msg", "변경이 완료되었습니다");
            return "redirect:/members/read";
        } else {
            model.addAttribute("errorMessage", "변경 실패");
            return "members/modify";
        }

    }

    @GetMapping("/findPw")
    public void findPw(){

    }

    @GetMapping("/checkIdEmail")
    public @ResponseBody String check(@RequestParam("id") String id, @RequestParam("email") String email){
        log.info(email);
        MemberUserDTO memberUserDTO = memberUserService.emailCheck(id, email);
        log.info(memberUserDTO);
         if (memberUserDTO != null){
             log.info("진실이다");
            return "true";
        } else {
             log.info("거짓이다");
            return "false";
        }
    }

    @GetMapping("/sendEmail")
    @ResponseBody
    public String test(HttpServletRequest request, String id, String email) throws IOException {
        UUID uuid = UUID.randomUUID();
        Random random = new Random();
        String pw = uuid.toString() + (random.nextInt(888888) + 111111);
        log.info(pw);
        pw = pw.substring(0,pw.indexOf("-"));
        log.info(pw);
        String title = "오늘 뭐 해? 사이트의 임시 비밀번호 입니다.";
        String from = "aaa@gmail.com";
        String to = email;
        String content =
                System.getProperty("line.separator")+
                        System.getProperty("line.separator")+
                        "해당 임시 비밀번호로 로그인 후 반드시 비밀번호를 다시 변경해 주시기 바랍니다."
                        +System.getProperty("line.separator")+
                        System.getProperty("line.separator")+
                        "임시 비밀번호는 " + pw + " 입니다. "
                        +System.getProperty("line.separator");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(title);
            messageHelper.setText(content);

            mailSender.send(message);
            memberUserService.savePw(id, pw);

        } catch (Exception e) {
            System.out.println(e);
        }
        return "전송";
    }

    @GetMapping("/remove")
    public String removeMember(@RequestParam("mno") Long mno){
        memberUserService.memberRemove(mno);
        return "redirect:/members/logout";
    }

    }



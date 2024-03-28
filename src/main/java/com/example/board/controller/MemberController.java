package com.example.board.controller;

import com.example.board.dto.MemberDTO;
import com.example.board.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    // 생성자 주입 방식
    /*
    자바에서 주로 사용하는 방식
    MemberService memberService = new MemberService();
    memberService.save();
    객체 생성 후, 객체를 통해서 메소드를 접근하는 방식

    Spring에서는 해당 방식 대신 생성자 주입 방식 활용함
     */
    private final MemberService memberService;
    @GetMapping("/save")
    public String saveForm() {
        return "saveMember";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("MemberDTO = " + memberDTO);
        // DB에 저장
        memberService.save(memberDTO);
        return "login";
    }

    @GetMapping("/save1")
    public String save1(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("MemberDTO = " + memberDTO);
        // DB에 저장
        memberService.save(memberDTO);
        return "login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if(loginResult != null) {
            // login 성공

            // HttpSession를 사용하여 로그인 상태 유지
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return "main";
        } else {
            return "login";
        }
    }

    @GetMapping
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        // view에 가져갈 데이터를 model에 담아서 사용
        model.addAttribute("memberList", memberDTOList);

        return "listMember";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        System.out.println("findById : " + id);
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        System.out.println(memberDTO);
        return "detailMember";
    }

}
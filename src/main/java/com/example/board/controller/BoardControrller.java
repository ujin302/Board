package com.example.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // RestController로는 안됨...ㅠ
@RequiredArgsConstructor
@RequestMapping("/board") // 주소가 posting 시작되는 값 설정
public class BoardControrller {
    // 생성자 주입 방식
//    private final BoardService boardService;
    @GetMapping("/save") // 주소 요청 : board/save
    public String saveFrom() {
        return "save"; // save.html 호출
    }

//    @PostMapping("/save")
//    public String save(@ModelAttribute BoardDTO boardDTO) {
//        // BoardDTO에 정의되어 있는 값들 받음
//        System.out.println("BoardDTO = " + boardDTO);
//        postingService.save(postingDTO);
//        return "index";
}

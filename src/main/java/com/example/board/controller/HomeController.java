package com.example.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {
    @GetMapping("/") // 기본 주소
    public String index() {
        return "index"; // index.html 파일 불러옴
    }
}

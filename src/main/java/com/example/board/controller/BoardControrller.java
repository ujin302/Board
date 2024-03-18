package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // RestController로는 안됨...ㅠ
@RequiredArgsConstructor
@RequestMapping("/board") // 주소가 board 시작되는 값 설정
public class BoardControrller {
    // 생성자 주입 방식
     private final BoardService boardService;

    // 1. save.html 호출
    @GetMapping("/save") // 주소 요청 : board/save
    public String saveFrom() {
        return "save";
    }


    // 2. board/save 화면에서 '글작성' Button 클릭 시, DB에 데이터 저장 & index 화면 넘어감
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) {
        // BoardDTO에 정의되어 있는 값들 받음
        System.out.println("BoardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "index";
    }

    @GetMapping
    public String findAll(Model model) { // Model : Data를 가져와야할 때 사용
        System.out.println("list");
        // DB에서 전체 게시글 데이터를 가져와 list.html에 보여준다.
       List<BoardDTO> boardDTOList = boardService.findAll(); // 여러개의 Data를 가져오기 위해 List
       model.addAttribute("boardList", boardDTOList);
        return "list";
    }
}

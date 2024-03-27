package com.example.board.controller;

import com.example.board.dto.CommentDTO;
import com.example.board.repository.CommentRepository;
import com.example.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute CommentDTO commentDTO) {
        // @ResponseBody : ajax 문법
        System.out.println("commentDTO = " + commentDTO);

        // 1. 댓글 DB에 저장 & 저장한 Id 값 반환
        Long saveResult = commentService.save(commentDTO);

        // 2. 댓글 목록 반환
        if(saveResult != null) {
            // 2-1. 작성 성공 >> 댓글 목록을 가져와 리턴
            // 댓글 목록 : 해당 게시물의 모든 댓글
            List<CommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
            // Entity -> Header, Body 모두 다룸
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        }else {
            // 2-2. 작성 실패
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }
}

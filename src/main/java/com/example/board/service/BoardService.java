package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.BoardEntity;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// DTO -> Entity (Entity Class)
// Entity -> DTO (DTO Class)
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 1. DB에 Data 저장
    public void save(BoardDTO boardDTO) {
        // DTO의 값들을 Entity에 옮겨담는 함수 호출
        BoardEntity boardEntity = BoardEntity.toSaveEntitiy(boardDTO);
        // save : JPA가 가지고 있는 함수
        // DB에 저장
        boardRepository.save(boardEntity);
    }
}

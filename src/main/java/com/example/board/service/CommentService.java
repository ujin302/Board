package com.example.board.service;

import com.example.board.dto.CommentDTO;
import com.example.board.entity.BoardEntity;
import com.example.board.entity.CommentEntity;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository; // 객체 변환 시, BoardEntity 필요함




    public Long save(CommentDTO commentDTO) {
        // save() : 댓글 DB에 저장 & 저장한 댓글 Id 반환

        // boardId 값에 해당하는 부모 Entity(BoardEntity) 가져오기
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if(optionalBoardEntity.isPresent()) { // 값 존재 O
            // BoardEntity 추출
            BoardEntity boardEntity = optionalBoardEntity.get();
            // 객체 변환 : DTO -> Entity
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity);
            // DB(tb_board_comment)에 저장 & id(댓글) 값 반환
            return commentRepository.save(commentEntity).getId();
        } else { // 값 존재 X
            return null;
        }
    }

/*    혼자 개발해본 코드
    @Transactional
    public List<CommentDTO> findAll(Long boardId) {
        List<CommentEntity> commentEntityList = commentRepository.findAll();
        >> boardRepository 를 사용하여 해당하는 Row 를 가져올수 있음

        List<CommentDTO> commentDTOList = new ArrayList<>();

        for(CommentEntity commentEntity : commentEntityList) {
            if(Objects.equals(commentEntity.getBoardEntity().getId(), boardId)) {
                commentDTOList.add(CommentDTO.toCommentDTO(commentEntity));
            }
        }
        return commentDTOList;
    }
*/
    @Transactional
    public List<CommentDTO> findAll(Long boardId) {
        // findAll() : 게시물 id 값을 사용하여 해당 게시물의 댓글 목록 리턴

        /*
            select * from tb_board_comment where board_id = ? order by id desc;
            1. 게시물 id 기준으로 댓글 목록 리턴
            2. 댓글 목록 id 기준으로 내림차순
         */
        BoardEntity boardEntity = boardRepository.findById(boardId).get(); // 댓글에 해당하는 BoardId 값의 boardEntity 추출
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity); // boardIb에 해당하는 모든 댓글 목록 추출

        // 객체 변환 : Entity -> DTO
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for(CommentEntity commentEntity : commentEntityList) {
            commentDTOList.add(CommentDTO.toCommentDTO(commentEntity));
        }
        return commentDTOList;
    }
}




















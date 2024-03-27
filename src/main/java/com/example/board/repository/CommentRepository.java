package com.example.board.repository;

import com.example.board.entity.BoardEntity;
import com.example.board.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    /*
            select * from tb_board_comment where board_id = ? order by id desc;
            findAllByBoardEntity : select * from tb_board_comment where board_id = ?
            OrderByIdDesc : order by id desc;
            
            대소문자가 정말  중요함! 
            대소문자가 맞지 않는 경우, 쿼리문 오류 발생
         */
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);
}
package com.example.board.entity;

// 파일 정보를 가지고 있는 Table

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_BoardFile")
public class BoardFileEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    // 연관관계 : 자식
    // 게시물 1개에 파일 N개 저장
    // N : 1 => 파일 N개 : 게시물 1개
    @ManyToOne(fetch = FetchType.LAZY) // 부모 조회 시, 자식까지 들고옴. 해당 옵션을 사용할 경우, 자식을 사용할때만 불러온다는 의미!
    @JoinColumn(name = "board_id") // 
    private BoardEntity boardEntity; // 실제 DB에서는 Long 형식으로 저장되어 있음

    public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity, String originalFileName, String storedFileName) {
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity);

        return boardFileEntity;
    }
}

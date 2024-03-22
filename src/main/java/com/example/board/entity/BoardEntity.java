package com.example.board.entity;

import com.example.board.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.*;

// DB의 테이블 역할을 하는 클래스

// Entity : JPA가 관리하는 클래스
// JPA : 객체와 관계형 데이터 베이스를 매핑해주는 기술
@Getter
@Setter
@Builder // 모든 요소를 받는 package-private 생성자가 자동으로 생성
@NoArgsConstructor // 파라미터가 없는 디폴트 생성자를 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 생성
@Entity // 해당 클래스를 JPA가 관리
@Table(name = "tb_Board")
public class BoardEntity extends BaseEntity{
    // 1. Column 생성

    @Id // pk(기본키) 컬림 지정 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment : 데이터가 삽입될 때마다 1씩 증가
    private Long id;

    // 컬럼명을 생략하면 필드명을 컬럼명으로 사용
    @Column(length = 20, nullable = false) // 크기 20, not null
    private String boardWriter;

    @Column // 크기 255, null ㅇ
    private String boardPass;

    @Column
    private String boardTitle;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;

    @Column
    private int fileAttached; // 파일 있으면 1 없으면 0

    // 2. Data Entitiy에 저장
    // DTO의 값들을 Entity에 옮겨담는 함수 생성
    public static BoardEntity toSaveEntitiy(BoardDTO boardDTO) {
        // id값(기본키)가 없음 -> repository에서 insert로 인식
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(0); // 첨부 파일 X
        return boardEntity;
    }

    public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {
        // id값(기본키)가 있음 -> repository에서 update로 인식
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        return boardEntity;
    }
}

package com.example.board.dto;

// DTO(Data Transfer Object), VO, Bean -> 3개다 비슷함
// 객체를 담을 클래스

import com.example.board.entity.BoardEntity;
import com.example.board.entity.BoardFileEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// lombok이 가지고 있는 기능
@Getter// 각각의 필드에 대해 Get에 대한 메서드 자동 생성
@Setter // 각각의 필드에 대해 Set에 대한 메서드 자동 생성
@ToString
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class BoardDTO {
    // html에 태그에 있는 name과 해당 변수들이 동일하면 자동으로 해당 변수에 값 넘어옴

    // 게시물 기본 정보
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    // 파일 관련 정보
    private List<MultipartFile> boardFile; // save.html -> Controller 파일 담는 용도
    // MultipartFile : 실제 파일을 담을 수 있는 인터페이스
    private List<String> originalFileName; // 원본 파일 이름
    private List<String> storedFileName; // 서버 저장용 파일 이름
    // 사용자가 같은 이름의 파일을 올릴 가능성이 있기에 저장용 파일명이 필요하다
    private int fileAttached; // 파일 첨부 여부 (첨부 1, 미첨부 0)
    // Bool 타입일 경우 Entity에서 설정해야 하는 부분들이 많아짐 -> 해당 부분은 찾아보기



    // 게시물 관련 Data
    public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    // 객체 변환 : Entity -> DTo
    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());
        boardDTO.setFileAttached(boardEntity.getFileAttached());

        if(boardEntity.getFileAttached() == 1) { // 파일 있음
            /* 단일 첨부 파일
            // 파일 이름 가져오기
            // OriginalFileName, StoredFileName : tb_board_file(BoardFileEntity)
            boardDTO.setOriginalFileName(boardEntity.getBoardFileEntityList().get(0).getOriginalFileName());
            boardDTO.setStoredFileName(boardEntity.getBoardFileEntityList().get(0).getStoredFileName());
             */

            // 다중 파일 첨부
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();

            for(BoardFileEntity boardFileEntity : boardEntity.getBoardFileEntityList()) {
                // List에 파일명을 하나씩 담음
                originalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
                System.out.println("originalFileName : " + boardFileEntity.getOriginalFileName() + "& storedFileNameList : " + boardFileEntity.getStoredFileName());

            }
            boardDTO.setOriginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);
        }



        return boardDTO;
    }
}

package com.example.board.dto;

// DTO(Data Transfer Object), VO, Bean -> 3개다 비슷함
// 객체를 담을 클래스


import lombok.*;

import java.time.LocalDateTime;

// lombok가 가지고 있는 기능
@Getter// 각각의 필드에 대해 Get에 대한 메서드 자동 생성
@Setter // 각각의 필드에 대해 Set에 대한 메서드 자동 생성
@ToString
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class BoardDTO {
    // html에 태그에 있는 name과 해당 변수들이 동일하면 자동으로 해당 변수에 값 넘어옴
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;
}

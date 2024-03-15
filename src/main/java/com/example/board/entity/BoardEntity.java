package com.example.board.entity;

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
@Table(name = "tb_board")
public class BoardEntity {
//    @Id // pk(기본키) 컬림 지정 필수
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment : 데이터가 삽입될 때마다 1씩 증가
//    @Column(name = "id",nullable = false)
//    private Integer id;
    // 컬럼명을 생략하면 필드명을 컬럼명으로 사용
//    private String writer;
@Id // pk(기본키) 컬림 지정 필수
@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
private Long id;

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
}

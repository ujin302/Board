package com.example.board.repository;

import com.example.board.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // 이메일로 회원 정보 조회
    // Optional : Null 값 방지
    Optional<MemberEntity> findByMemberEmail(String memderEmail);
}

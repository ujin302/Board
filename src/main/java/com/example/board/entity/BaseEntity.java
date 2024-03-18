package com.example.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    // 실행 시간
    @CreationTimestamp
    @Column(updatable = false) // 수정 시 관련 X
    private LocalDateTime createdTime;

    // 업데이트 시간
    @UpdateTimestamp
    @Column(insertable = false) // 입력 시 관련 X
    private  LocalDateTime UpdatedTime;
}

package com.example.board.repository;


import com.example.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    //update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=?
    // Query : JPA에서 제공, 쿼리문을 사용할 수 있음
    // Modifying : Update, Delete 문 실행 시 필수 사용
    @Modifying
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id")
    void updateHits(@Param("id") Long id);
}

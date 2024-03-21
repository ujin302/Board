package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.BoardEntity;
import com.example.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DTO -> Entity (Entity Class)
// Entity -> DTO (DTO Class)
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 1. DB에 Data 저장
    public void save(BoardDTO boardDTO) {
        // DTO의 값들을 Entity에 옮겨담는 함수 호출
        BoardEntity boardEntity = BoardEntity.toSaveEntitiy(boardDTO);
        // save : JPA가 가지고 있는 함수
        // DB에 저장
        boardRepository.save(boardEntity);
    }

    // 2. List DTO 객체에 저장
    public List<BoardDTO> findAll() {
        // Repository에 Data를 들고 올떄는 대부분 Entity로 들고 온다!
        // findAll : 모든 DB 데이터 들고 오기
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();

        // Entity 객체를 DTO에 담기
        for(BoardEntity boardEntity: boardEntityList) {
            System.out.println(BoardDTO.toBoardDTO(boardEntity));
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }


    @Transactional // 데이터의 일관성을 위해 사용
    // 트랜잭션 시작 -> 메서드 정상 종료 -> Commit
    // 트랜잭션 시작 -> 메서드 비정상 종료 -> rollback
    // 일부 작업만 데이터베이스에 반영되는 것을 방지하여 데이터 일관성 유지!

    // 3. 조회수 +1
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    // 4. id가 있을 경우, 해당 데이터를 가지고 있는 DTO 반환
    public BoardDTO findById(Long id) {
        // 방법 1. Optional 형태로 데이터 받기
        // Optional : NPE(NullPoinrerException) 방지, null 반환 시, 오류 발생 가능성이 높은 경우 사용
        // findById : 리턴 값 Optional 형태, Table에서 기본키에 해당하는 Row의 데이터 반환
//        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
//        if(optionalBoardEntity.isPresent()) { // isPresent : 객체가 값을 가지고 있으면 true
//            BoardEntity boardEntity = optionalBoardEntity.get(); // DB의 데이터를 가져와 Entity 객체에 저장
//            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity); // Entity 객체를 DTO 객체에 저장
//            return boardDTO; // DTO 객체 반환
//        } else {
//            return null;
//        }

        // 방법 2. Entity 형태로 데이터 받기
        BoardEntity BoardEntity = boardRepository.findById(id).orElse(null);
        if(BoardEntity != null) {
            BoardDTO boardDTO = BoardDTO.toBoardDTO(BoardEntity); // Entity 객체를 DTO 객체에 저장
            return boardDTO; // DTO 객체 반환
        } else {
            return null;
        }
    }

    // 5. 게시글 수정
    public BoardDTO update(BoardDTO boardDTO) {
        // DB 데이터 수정을 위해 Entity로 변환
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);

        // save : insert 혹은 update 로 인식
        // id값(기본키)가 없으면 insert로 없으면 update로 인식함.
        boardRepository.save(boardEntity);

        return findById(boardDTO.getId());
        // 위에 있는 findById를 호출하여 DTO 객체 반환
        // 화면 상에 보여질 데이터는 DTO 객체 사용
    }

    // 6. 게시글 삭제
    public void delete(Long id) {
        boardRepository.deleteById(id); // 해당 id 값의 관련 데이터 삭제
    }

    // 7. 페이징 처리
    public Page<BoardDTO> paging(Pageable pageable) {
        // 한페이지당 3개씩 아이템을 보여줌 & id 컬럼 기준으로 내리차순 정렬
        int page = pageable.getPageNumber() - 1; // page 위치에 있는 값은 0부터 시작
        int pageLimit = 3; // 한 페이지에 보여줄 아이템 개수

        // 페이징 처리된 데이터 가져오기!
        Page<BoardEntity> boardEntities = boardRepository.findAll(
                PageRequest.of(
                        page, // 0 부터 시작함. 따라서 화면에 10 페이지를 요청하면 9 페이지로 데이터를 찾아야함
                        pageLimit,
                        Sort.by(Sort.Direction.DESC, "id") // id 컬럼을 기준으로 하여 내림차순 정렬
                )
        );

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        // 객체 변환 필요 : Entity -> DTO
        // findAll 함수 처럼 List<BoardDTO> 형태로 받으면 Page 객체에서 사용할 수 있는 메소드를 사용할 수 없음.
        // 따라서 Page<BoardDTO> 객체로 변환할거임.
        Page<BoardDTO> boardDTOS = boardEntities.map(
                // board : Entity 객체
                // map 메소드 : for문 처럼 하나씩 뽑아옴
                board -> new BoardDTO(
                        // 목록 : id, Writer, Title, Hits, CreatedTime
                        board.getId(),
                        board.getBoardWriter(),
                        board.getBoardTitle(),
                        board.getBoardHits(),
                        board.getCreatedTime()
                )
        );
        // boardDTOS : 'id, Writer, Title, Hits, CreatedTime' 을 가지고 있으며 Page 객체의 메소드 활용 가능
        return boardDTOS;
    }
}














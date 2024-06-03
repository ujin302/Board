package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.BoardEntity;
import com.example.board.entity.BoardFileEntity;
import com.example.board.repository.BoardFileRepository;
import com.example.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DTO -> Entity (Entity Class)
// Entity -> DTO (DTO Class)
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    // 1. DB에 Data 저장
    public void save(BoardDTO boardDTO, String userName) throws IOException {
        // throws IOException : save 메소드에서 발생한 예외를 상위 메소드인 BoardService 에서 처리하기 위해 사용Z
        // 상위에서 처리하는게 더 올바른 경우도 있고 해당 메소드에서 처리하는게 더 올바른 경우가 있다!

        boardDTO.setBoardWriter(userName);
        // 파일 첨부 여부에 따라 로직 분리
        if(boardDTO.getBoardFile().isEmpty() || boardDTO.getBoardFile().get(0).isEmpty()) {
            // 첨부 파일 X

            // 1. DTO의 값들을 Entity에 옮겨담는 함수 호출
            BoardEntity boardEntity = BoardEntity.toSaveEntitiy(boardDTO);

            // 2. DB에 저장 (tb_board)
            // save : JPA가 가지고 있는 함수
            boardRepository.save(boardEntity);
        } else {
            // 파일 첨부 ㅇ
            /*
                1. DTO에 담긴 파일 추출 (실제 파일)
                2. 파일의 이름  추출
                3. 서버 저장용 이름 생성
                    : 내사진.jpg -> 8397980824_내사진.jpg
                4. 저장 경로 설정
                5. 해당 경로에 파일 저장
                6. tb_Board 에 해당 데이터 save 처리 (-> 게시물 관련 Data)
                7. tb_Board_File 에 해당 데이터 save 처리 (-> 첨부파일 관련 Data)
             */
            /* 단일 첨부 파일
            MultipartFile boardFile = boardDTO.getBoardFile(); // 1
            String originalFilename = boardFile.getOriginalFilename(); // 2
            String storedFileName = System.currentTimeMillis() + " " + originalFilename; // 3
            String savePath = "E:/Board_File/" + storedFileName; // 4. 실제 존재하는 경로 값
            boardFile.transferTo(new File(savePath)); // 5
            // transferTo : 파일 저장 메소드 & 예외 발생 가능성으로 인해서 throws IOException 사용

            // 6
            BoardEntity boardEntity = BoardEntity.toSaveFileEntitiy(boardDTO); // 6-1. DTO -> Entity (id값이 없다)
            Long savedId = boardRepository.save(boardEntity).getId(); // 6-2. Entity 객체 저장(tb_board) & 저장한 id 가져오기
            System.out.println("저장한 ID (첨부파일 O ) : " + savedId);
            BoardEntity board = boardRepository.findById(savedId).get(); // 6-3. 저장한 id에 대한 Data 가져오기

            // 7
            BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName); // 7-1. File Entity에 저장
            boardFileRepository.save(boardFileEntity); // 7-2. DB에 저장 (tb_board_file)
             */

            // 다중 첨부 파일
            // 6
            BoardEntity boardEntity = BoardEntity.toSaveFileEntitiy(boardDTO); // 6-1. DTO -> Entity (id값이 없다)
            Long savedId = boardRepository.save(boardEntity).getId(); // 6-2. Entity 객체 저장(tb_board) & 저장한 id 가져오기
            System.out.println("저장한 ID (첨부파일 O ) : " + savedId);
            BoardEntity board = boardRepository.findById(savedId).get(); // 6-3. 저장한 id에 대한 Data 가져오기

            for (MultipartFile boardFile : boardDTO.getBoardFile()) {
                String originalFilename = boardFile.getOriginalFilename(); // 2
                String storedFileName = System.currentTimeMillis() + " " + originalFilename; // 3
                String savePath = "E:/Board_File/" + storedFileName; // 4. 실제 존재하는 경로 값
                boardFile.transferTo(new File(savePath)); // 5
                // transferTo : 파일 저장 메소드 & 예외 발생 가능성으로 인해서 throws IOException 사용

                // 7
                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName); // 7-1. File Entity에 저장
                boardFileRepository.save(boardFileEntity); // 7-2. DB에 저장 (tb_board_file)
            }
        }
    }

    // 2. List DTO 객체에 저장
    @Transactional // 부모 Entity에서 자식 Entity 접근 시(toBoardDTO) 필수 사용!

    public List<BoardDTO> findAll() {
        // Repository에 Data를 들고 올떄는 대부분 Entity로 들고 온다!
        // findAll : 모든 DB 데이터 들고 오기
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();

        // Entity 객체를 DTO에 담기
        for(BoardEntity boardEntity: boardEntityList) {
            // toBoardDTO 함수에서 부모 Entity를 통해 자식 Entity 접근
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
    @Transactional // 부모 Entity에서 자식 Entity 접근 시(toBoardDTO) 필수 사용!
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
            // 1. 게시물 정보 객체 변환 : Entity -> DTO
            // toBoardDTO 함수에서 부모 Entity를 통해 자식 Entity 접근
            BoardDTO boardDTO = BoardDTO.toBoardDTO(BoardEntity);



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














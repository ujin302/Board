package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.CommentDTO;
import com.example.board.dto.MemberDTO;
import com.example.board.service.BoardService;
import com.example.board.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

// 컨트롤러란?
// 화면과 비즈니스 로직을 연결시키는 다리 역할
// 주소 요청 받아 들이고 어디로 연결시켜주는 역할
@Controller // RestController로는 안됨...ㅠ
@RequiredArgsConstructor
@RequestMapping("/board") // 주소가 board 시작되는 값 설정
public class BoardControrller {
    // 생성자 주입 방식
    private final BoardService boardService;
    private final CommentService commentService;


    // 1. save.html 호출
    @GetMapping("/save") // 주소 요청 : board/save
    public String saveFrom() {
        return "save";
    }


    // 2. board/save 화면에서 '글작성' Button 클릭 시, DB에 데이터 저장 & main 화면 넘어감
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO, HttpSession session) throws IOException {
        // ModelAttribute : BoardDTO에 정의되어 있는 값들 받음
        System.out.println("BoardDTO = " + boardDTO);
        System.out.println("BoardDTO = " + session.getAttribute("loginUserName"));

        // DB에 데이터 저장
        boardService.save(boardDTO, session.getAttribute("loginUserName").toString());

        // main.html 화면 이동
        return "main";
    }

    // 3. list.html에 목록 데이터 뿌리기
    @GetMapping
    public String findAll(Model model) {
        // Model : Data를 가져와야할 때 사용
        // Controller의 메소드에서 Model 타입의 객체를 매개변수로 받을 수 있다.

        // 3-1. DB에서 전체 Data 가져와 DTO 객체에 저장
         List<BoardDTO> boardDTOList = boardService.findAll();

         // 3-2. boardList라는 이름으로 DTO 객체 전송
         model.addAttribute("boardList", boardDTOList);

         // 3-3. list.html 화면 띄우기
         return "list";
    }

    // 4. detail.html에 상세 화면 뿌리기
    @GetMapping("/{id}") // board/id
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page=1) Pageable pageable) {
        // @PathVariable : 경로 상에 있는 값을 가져올 때 사용
        // Model : spring이 지원하는 기능으로 HashMap (Key, Vaule 존재)

        /*
            게시글 상세 페이지
            1. 해당 게시글의 조회수 +1
            2. 게시글 데이터를 가져와서 detail.html에 출력
         */

        // 1-1. 조회수 +1
        boardService.updateHits(id);
        // 1-2. 데이터 뿌리기
        BoardDTO boardDTO = boardService.findById(id);
        List<CommentDTO> commentDTOList = commentService.findAll(boardDTO.getId());
        model.addAttribute("commentList", commentDTOList);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());

        return "detail";
    }

    // 5. 게시글 수정
    /*
            1. 상세화면에서 수정 버튼 클릭
            2. 서버에서 해당 게시글의 정보를 가지고 수정 화면(update.html) 출력
            3. 제목, 내용 수정 입력 받아서 서버로 요청
            4. 수정 처리
    */

    // 5-1. 서버에서 해당 게시글의 정보를 가지고 수정 화면(update.html) 출력
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id); // 해당 id값에 대한 정보를 DTO 객체에 담음
        model.addAttribute("boardUpdate", boardDTO); // Data 전달

        return "update";
    }

    // 5-2. 제목, 내용 수정 입력 받아서 서버로 요청
    @PostMapping("/update")
//    update.html에서 "/board/update"으로 호출함
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {

        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);

        return "detail"; // 수정 후, 수정이 반영된 상세 페이지
//        return "redirect:/board/" + boardDTO.getId();
        // findById 메소드 호출 -> 해당 함수에 조회수 +1 로직으로 인해 조회수에 영향을 미칠 수 있음.
        // 따라서 첫번째 방식으로 사용
    }

    //6. 게시글 삭제
    /*
        1. 삭제 버튼 클릭
        2. DB에서 데이터 삭제
        3. 목록 화면 보여주기
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);

        return "redirect:/board";
    }

    //7. 페이징 처리
    /*
        1. 페이지 정보 리턴
        2. 하단 페이지 관련 변수 설졍
     */
    // /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
        // @PageableDefault(page = 1) -> 디폴트 1 설정
        // pageable -> page 번호 가져옴

        // 페이지 화면에 보여질 아이템 개수와 그에 따른 페이지 개수가 필요함
        // 현재 로직에서는 지정되어 있는 아이템 개수로 페이징 처리할 거임
        // 개수 지정에 관련한 것들은 추후에 개발
//        pageable.getPageNumber(); // 현재 보고서 페이지 번호를 리턴
        // 1. 페이지 정보 리턴
        Page<BoardDTO> boardList = boardService.paging(pageable);

        // 2. 하단 페이지 관련 변수 설졍
        int blockLimit = 3; // 하단에 보이는 페이지 개수
        // 현재 페이지가 2 or 3 일 경우 시작 페이지 1
        // 현재 페이지가 8 or 9 일 경우 시작 페이지 7
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~

        // 총 페이지가 8일 경우 마지막 숫자는 8로 나올 수 있도록
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        // 현재 사용자 : 3
        // 1 2 3
        // 현재 사용자 : 7
        // 7 8 9

        // 3. Model 객체에 필요 정보 담기
        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "paging";
    }

    // 8. 파일(이미지) 첨부하기
    /*
        파일 첨부와 관련하여 추가될 부분들
        - save.html
            -> 글 작성 시, 파일 첨부
        - BoardDTo
            -> 첨부 파일 Data 전달
        - BoardService.save()
            -> 첨부 파일 확인
            -> 첨부 파일 저장 공간에 저장
        - BoardEntity
            -> 파일과 관련된 컬럼 추가
        - detail.html
            -> 상세페이지에 첨부 파일 있으면 보여주기
        - BoardFileEntity, BoardFileRepository 자바 클래스 추가
            -> DB에는 파일 자체를 저장하는 것이 아닌 파일명칭만 저장함
            -> 파일 자체는 서버 안에 저장
     */

}
















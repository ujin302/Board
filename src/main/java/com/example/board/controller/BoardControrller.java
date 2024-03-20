package com.example.board.controller;

import com.example.board.dto.BoardDTO;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    // 1. save.html 호출
    @GetMapping("/save") // 주소 요청 : board/save
    public String saveFrom() {
        return "save";
    }


    // 2. board/save 화면에서 '글작성' Button 클릭 시, DB에 데이터 저장 & index 화면 넘어감
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) {
        // ModelAttribute : BoardDTO에 정의되어 있는 값들 받음
        System.out.println("BoardDTO = " + boardDTO);
        // DB에 데이터 저장
        boardService.save(boardDTO);

        // index.html 화면 이동
        return "index";
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
    public String findById(@PathVariable Long id, Model model) {
        // @PathVariable : 경로 상에 있는 값을 가져올 때 사용
        // Model : spring이 지원하는 기능으로 HashMap (Key, Vaule 존재)

        /*
            게시글 상세 페이지
            1. 해당 게시글의 조회수 +1
            2. 게시글 데이터를 가져와서 detail.html에 출력
         */

        // 4-1. 조회수 +1
        boardService.updateHits(id);
        // 4-2. 데이터 뿌리기
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);

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
        Page<BoardDTO> boardList = boardService.paging(pageable);


        return "list";
    }

}
















package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.dto.MemberDTO;
import com.example.board.entity.MemberEntity;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    // 생성자 주입
    private final MemberRepository memberRepository;

    // 1. DB에 회원정보 저장
    public void save(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toSaveEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    public MemberDTO login(MemberDTO memberDTO) {
        /*
            1. 회원이 입력한 이메일로 DB에서 조회
            2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
         */

        /*
        1. Optional 타입 사용 O
            Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());

            if(byMemberEmail.isPresent()) {
                // 조회 결과 데이터 존재 O
                MemberEntity memberEntity = byMemberEmail.get();
                // byMemberEmail.get(): get 메소드를 사용하여 Optional 객체 안에 있는 Entity 객체 추출

                if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                    // 비밀번호 일치
                    MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                    return dto;
                }else {
                    // 비밀번호 불일치
                    return null;
                }
            } else {
                // 조회 결과 데이터 존재 X
                return null;
            }
         */

        // 2. Optional 타입 사용 X
        MemberEntity memberEntity = memberRepository.findByMemberEmail(memberDTO.getMemberEmail()).orElse(null);
        if(memberEntity != null && memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
            MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
            return dto;
        }else {
            return null;
        }
    }

    public List<MemberDTO> findAll() {
        // DB에 있는 모든 회원 정보 가져오기
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();

        for(MemberEntity memberEntity : memberEntityList) {
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
            System.out.println(MemberDTO.toMemberDTO(memberEntity));
        }

        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);

        if(optionalMemberEntity.isPresent()) {
            MemberDTO memberDTO = MemberDTO.toMemberDTO(optionalMemberEntity.get());

            return memberDTO;
        }else {
            return null;
        }
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberDTO updateForm(String myEmail) {
        // Email를 통해서 회원정보 가져오기
        MemberEntity memberEntity = memberRepository.findByMemberEmail(myEmail).orElse(null);
        if(memberEntity != null) {
            MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
            return memberDTO; // DTO 객체 반환
        } else {
            return null;
        }
    }

    public MemberDTO update(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toUpdateEntity(memberDTO);
        memberRepository.save(memberEntity);
        System.out.println(memberEntity);

        return findById(memberDTO.getId());
    }

    public Boolean emailCheck(String memberEmail) {
        MemberEntity memberEntity = memberRepository.findByMemberEmail(memberEmail).orElse(null);

        if(memberEntity != null && memberEntity.getMemberEmail().equals(memberEmail)) {
            // 결과 존재 -> 사용 X
            return Boolean.FALSE;
        }else {
            // 결과 미존재 -> 사용 O

            return Boolean.TRUE;
        }
    }
}






















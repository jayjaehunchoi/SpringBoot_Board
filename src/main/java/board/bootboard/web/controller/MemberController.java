package board.bootboard.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import board.bootboard.util.annotation.Login;
import board.bootboard.util.annotation.TimeChecker;
import board.bootboard.domains.member.Member;
import board.bootboard.web.dto.member.MemberDto;
import board.bootboard.service.MemberService;
import board.bootboard.web.dto.member.MemberLoginDto;
import board.bootboard.web.dto.common.ResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

import static board.bootboard.util.constant.SessionConst.SESSION_ID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @TimeChecker
    @PostMapping
    public ResponseEntity<?> signUp(@RequestBody @Validated MemberDto dto){
        Member member = new Member(dto);
        memberService.join(member);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody @Validated MemberLoginDto dto, HttpServletRequest request){
        Member findMember = memberService.checkLoginInfoCorrect(dto.getName(),dto.getPassword());
        createLoginSession(request, findMember);
        MemberDto memberDto = new MemberDto(findMember);
        return new ResponseEntity<>(memberDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> findAll(){
        List<Member> members = memberService.findAllMembers();
        ResponseDto dto = createAllMembersDto(members);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "로그아웃 성공";
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/my")
    public MemberDto myPage(@Login Member member){
        MemberDto memberDto = new MemberDto(member);
        return memberDto;
    }

    private ResponseDto createAllMembersDto(List<Member> members) {
        List<MemberDto> responseDto = members.stream().map(MemberDto::new).collect(Collectors.toList());
        return ResponseDto.<MemberDto>builder().data(responseDto).build();
    }

    private void createLoginSession(HttpServletRequest request, Member findMember) {
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_ID, findMember);
    }
}

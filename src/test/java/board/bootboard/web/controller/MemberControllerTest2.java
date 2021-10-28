package board.bootboard.web.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import board.bootboard.domains.member.Member;
import board.bootboard.service.MemberService;
import board.bootboard.web.dto.member.MemberDto;
import board.bootboard.util.exception.DuplicateEmailException;
import board.bootboard.util.exception.DuplicateIdException;
import board.bootboard.util.exception.LoginFailedException;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
public class MemberControllerTest2 {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MemberService memberService;

    @Test
    void 회원가입_성공() throws Exception {
        String member =  createMemberDto("wogns0107", "qwer12345!","wogns0108@nate.com");

        mvc.perform(post("/member/signup")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                .content(member)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void 회원가입_중복아이디_실패() throws Exception{
        // repository에 "wogns0108"이라는 아이디를 가진 멤버가 있다고 가정
        Member mockMember = new Member("wogns0108", "qwer12345!", "wogns0108@gmail.com", 25);
        doThrow(new DuplicateIdException("중복 아이디")).when(memberService).join(mockMember);

        String member =  createMemberDto("wogns0108", "qwer12345!","wogns0108@gmail.com");

        mvc.perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                        .content(member)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    void 회원가입_중복이메일_실패() throws Exception {
        // repository에 "wogns0108@naver.com"이라는 이메일을 가진 멤버가 있다고 가정
        Member mockMember = new Member("wogns0107", "qwer12345!", "wogns0108@naver.com", 25);
        doThrow(new DuplicateEmailException("중복 이메일")).when(memberService).join(mockMember);

        String member =  createMemberDto("wogns0107", "qwer12345!","wogns0108@naver.com");

        mvc.perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                        .content(member)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 로그인성공() throws Exception{
        Member mockMember = new Member("wogns0108", "qwer12345!", "wogns0108@naver.com", 25);

        given(memberService.checkLoginInfoCorrect("wogns0108","qwer12345!")).willReturn(mockMember);
        String member = createMemberDto("wogns0108","qwer12345!","wogns0108@naver.com");
        mvc.perform(post("/member/login").contentType(MediaType.APPLICATION_JSON)
                .content(member)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.name == '%s')]","wogns0108").exists())
                .andDo(print());

    }

    @Test
    void 로그인_실패_존재하지않는아이디() throws Exception{

        given(memberService.checkLoginInfoCorrect("wogns0107","qwer12345!")).willThrow(new LoginFailedException("존재하지 않는 아이디 입니다."));

        String member = createMemberDto("wogns0107","qwer12345!","wogns0108@naver.com");
        mvc.perform(post("/member/login").contentType(MediaType.APPLICATION_JSON)
                        .content(member)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.[?(@.error == '%s')]","존재하지 않는 아이디 입니다.").exists());
    }

    @Test
    void 로그인_실패_비밀번호틀림() throws Exception{
        given(memberService.checkLoginInfoCorrect("wogns0108","qwer121345!")).willThrow(new LoginFailedException("비밀번호가 틀렸습니다."));

        String member = createMemberDto("wogns0108","qwer121345!","wogns0108@naver.com");
        mvc.perform(post("/member/login").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(member))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[?(@.error == '%s')]","비밀번호가 틀렸습니다.").exists())
                .andDo(print());

    }
    private String createMemberDto(String id,String password, String email) throws JsonProcessingException {
        MemberDto memberDto = MemberDto.builder().name(id)
                            .password(password)
                            .age(25)
                            .email(email)
                            .build();
        return objectMapper.writeValueAsString(memberDto);
    }



}

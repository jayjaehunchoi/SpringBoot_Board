package board.bootboard.web.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import board.bootboard.domains.member.Member;
import board.bootboard.repository.MemberRepository;
import board.bootboard.web.dto.member.MemberDto;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class MemberControllerTest {

    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .build();
        memberRepository.save(new Member("wogns0108", passwordEncoder.encode("qwer12345!") ,"wogns0108@naver.com",25));
    }

    @AfterEach
    void tearDown(){
        memberRepository.deleteAll();
    }

    @Test
    void ????????????_??????() throws Exception {
        String member =  createMemberDto("wogns0107", "qwer12345!","wogns0108@nate.com");

        mvc.perform(post("/member/signup")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                .content(member)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        assertThat(memberRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void ????????????_???????????????_??????() throws Exception{
        String member =  createMemberDto("wogns0108", "qwer12345!","wogns0108@gmail.com");

        mvc.perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                        .content(member)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());

        assertThat(memberRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void ????????????_???????????????_??????() throws Exception {
        String member =  createMemberDto("wogns0107", "qwer12345!","wogns0108@naver.com");



        mvc.perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)
                        .content(member)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());

        assertThat(memberRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void ???????????????() throws Exception{
        String member = createMemberDto("wogns0108","qwer12345!","wogns0108@naver.com");
        mvc.perform(post("/member/login").contentType(MediaType.APPLICATION_JSON)
                .content(member)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[?(@.name == '%s')]","wogns0108").exists())
                .andDo(print());

    }

    @Test
    void ?????????_??????_???????????????????????????() throws Exception{
        String member = createMemberDto("wogns0107","qwer12345!","wogns0108@naver.com");
        mvc.perform(post("/member/login").contentType(MediaType.APPLICATION_JSON)
                        .content(member)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.[?(@.error == '%s')]","???????????? ?????? ????????? ?????????.").exists());
    }

    @Test
    void ?????????_??????_??????????????????() throws Exception{
        String member = createMemberDto("wogns0108","qwer12111345!","wogns0108@naver.com");
        mvc.perform(post("/member/login").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(member))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[?(@.error == '%s')]","??????????????? ???????????????.").exists())
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

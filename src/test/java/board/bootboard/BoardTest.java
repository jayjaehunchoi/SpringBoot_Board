package board.bootboard;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import board.bootboard.domains.board.Board;
import board.bootboard.repository.BoardRepository;
import board.bootboard.domains.member.Member;
import board.bootboard.repository.MemberRepository;
import board.bootboard.web.dto.board.BoardSearchCond;
import board.bootboard.web.dto.board.MemberBoardDto;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class BoardTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @BeforeAll
    void setUp(){
        Member member = Member.builder().name("wogns0108")
                                .password("qwer12345!")
                                .age(25)
                                .email("wogns0108@naver.com")
                                .build();

        memberRepository.save(member);
        Member findMember = memberRepository.findByName("wogns0108").orElse(null);
        for(int i = 0 ; i < 30 ; i++){
            String region = "서울";
            if(i > 15)region = "부산";
            Board board = Board.builder().title("테스트" + i)
                                        .content("테스트 중 " + i)
                                        .likes(i)
                                        .region(region)
                                        .build();
            board.addMember(findMember);
            boardRepository.save(board);
        }

        Member member2 = Member.builder().name("qwer0107")
                .password("qwer12345!")
                .age(25)
                .email("qwer0108@naver.com")
                .build();

        memberRepository.save(member2);
        Member findMember2 = memberRepository.findByName("qwer0107").orElse(null);
        Board board = Board.builder().title("가평 사람 게시판 조회 테스트")
                .content("사람")
                .likes(5000)
                .region("가평")
                .build();
        board.addMember(findMember2);
        boardRepository.save(board);

    }

    @DisplayName("지역으로 조회")
    @Test
    void searchBoard(){
        BoardSearchCond boardSearchCond = new BoardSearchCond();
        boardSearchCond.setRegion("서울");

        List<MemberBoardDto> searchBoards = boardRepository.search(boardSearchCond);

        assertThat(searchBoards.size()).isEqualTo(16);

    }

    @DisplayName("이름으로 조회")
    @Test
    void searchBoardByName(){
        BoardSearchCond boardSearchCond = new BoardSearchCond();
        boardSearchCond.setName("qwer0107");

        List<MemberBoardDto> searchBoards = boardRepository.search(boardSearchCond);

        assertThat(searchBoards.size()).isEqualTo(1);

    }

    @DisplayName("인기 게시물 기준 90 이상")
    @Test
    void searchBoardByLikes(){
        BoardSearchCond boardSearchCond = new BoardSearchCond();
        boardSearchCond.setLikes(90);
        List<MemberBoardDto> searchBoards = boardRepository.search(boardSearchCond);

        assertThat(searchBoards.size()).isEqualTo(1);
    }

    @DisplayName("페이징")
    @Test
    void searchPageSimple(){
        BoardSearchCond boardSearchCond = new BoardSearchCond();
        boardSearchCond.setName("wogns0108");
        PageRequest pageRequest = PageRequest.of(0,5);
        Page<MemberBoardDto> memberBoardDtos = boardRepository.searchPageSimple(boardSearchCond, pageRequest);

        assertThat(memberBoardDtos.getSize()).isEqualTo(5);

    }

    @DisplayName("카운트 최적화 페이징")
    @Test
    void searchPageComplex(){
        BoardSearchCond boardSearchCond = new BoardSearchCond();
        boardSearchCond.setName("wogns0108");
        PageRequest pageRequest = PageRequest.of(0,10);
        Page<MemberBoardDto> memberBoardDtos = boardRepository.searchPageComplex(boardSearchCond, pageRequest);
        assertThat(memberBoardDtos.getSize()).isEqualTo(10);
    }

    @DisplayName("커서 페이징 첫번째 조회")
    @Test
    void searchPageCursor_first(){
        BoardSearchCond boardSearchCond = new BoardSearchCond();
        List<MemberBoardDto> dtos = boardRepository.searchByCursor(null, 10, boardSearchCond);
        assertThat(dtos.size()).isEqualTo(10);
        assertThat(dtos.get(9).getBoardId()).isEqualTo(22L);
    }

    @DisplayName("커서 페이징 viewMore 클릭시")
    @Test
    void searchPageCursor_whenClick(){
        BoardSearchCond boardSearchCond = new BoardSearchCond();
        List<MemberBoardDto> dtos = boardRepository.searchByCursor(22L, 10, boardSearchCond);
        assertThat(dtos.size()).isEqualTo(10);
        assertThat(dtos.get(9).getBoardId()).isEqualTo(12L);

    }

    @DisplayName("커서 페이징 남은 글이 10개 이하일때")
    @Test
    void searchPageCursor_whenLeftContentUnder10(){
        BoardSearchCond boardSearchCond = new BoardSearchCond();
        List<MemberBoardDto> dtos = boardRepository.searchByCursor(5L, 10, boardSearchCond);
        assertThat(dtos.size()).isEqualTo(4);
        assertThat(dtos.get(3).getBoardId()).isEqualTo(1L);

    }
}

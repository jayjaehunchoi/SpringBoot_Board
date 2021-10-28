package board.bootboard.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import board.bootboard.domains.board.Board;
import board.bootboard.service.BoardService;
import board.bootboard.domains.member.Member;
import board.bootboard.service.CommentService;
import board.bootboard.web.dto.board.comment.CommentResponseDto;
import board.bootboard.web.dto.member.MemberDto;
import board.bootboard.web.dto.common.ResponseDto;
import board.bootboard.web.dto.board.*;
import board.bootboard.util.annotation.Login;
import board.bootboard.util.annotation.LoginChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/boards")
@RestController
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final static int PAGE_SIZE = 10;

    @LoginChecker
    @PostMapping
    public ResponseEntity<?> write(@RequestBody @Validated BoardDto dto, @Login Member member){
        Board board = Board.builder().title(dto.getTitle())
                .content(dto.getContent())
                .region(dto.getRegion())
                .likes(dto.getLikes())
                .build();
        board.addMember(member);
        Board savedBoard = boardService.write(board);
        BoardResponseDto responseDto = new BoardResponseDto(savedBoard, new ArrayList<>());
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseDto findAllByOffsetPage(@RequestParam(defaultValue = "1") int page, @ModelAttribute BoardSearchCond condition){
        PageRequest pageRequest = PageRequest.of(page-1,PAGE_SIZE);
        Page<MemberBoardDto> boardByOffsetPage = boardService.findBoardByOffsetPage(condition, pageRequest);
        return ResponseDto.<MemberBoardDto>builder().total(boardByOffsetPage.getTotalPages()).data(boardByOffsetPage.getContent()).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findSelectedBoard(@PathVariable Long id, @RequestParam(required = false) Long comment){
        Board board = boardService.findOne(id);
        List<CommentResponseDto> commentResponseDtos = commentService.findBoardComments(comment, id).stream().map(CommentResponseDto::new).collect(Collectors.toList());
        BoardResponseDto responseDto = new BoardResponseDto(board, commentResponseDtos);
        return ResponseEntity.ok(responseDto);
    }

    @LoginChecker
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateSelectedBoard(@PathVariable Long id, @RequestBody @Validated BoardUpdateDto dto, @Login Member member){
        validateUpdateAuth(id, member);
        Board updateBoard = boardService.update(id, dto);
        List<CommentResponseDto> commentResponseDtos = commentService.findBoardComments(null, id)
                                                                    .stream()
                                                                    .map(CommentResponseDto::new)
                                                                    .collect(Collectors.toList());
        BoardResponseDto responseDto = new BoardResponseDto(updateBoard, commentResponseDtos);
        return ResponseEntity.ok(responseDto);
    }

    @LoginChecker
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSelectedBoard(@PathVariable Long id, @Login Member member){
        validateUpdateAuth(id,member);
        boardService.remove(id);
        commentService.deleteByBoardId(id);
        return ResponseEntity.ok().build();
    }


    private void validateUpdateAuth(Long id, Member member) {
        Board board = boardService.findOne(id);
        MemberDto boardWriter = new MemberDto(board.getMember());
        MemberDto sessionMember = new MemberDto(member);
        if(!sessionMember.equals(boardWriter)){
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "수정 / 삭제 권한이 없습니다.") {};
        }
    }
}

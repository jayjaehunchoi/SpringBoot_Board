package board.bootboard.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import board.bootboard.domains.board.Comment;
import board.bootboard.domains.member.Member;
import board.bootboard.service.CommentService;
import board.bootboard.util.annotation.Login;
import board.bootboard.util.annotation.LoginChecker;
import board.bootboard.web.dto.board.comment.CommentDto;
import board.bootboard.web.dto.board.comment.CommentResponseDto;
import board.bootboard.web.dto.common.GenerateResponseDto;

@Slf4j
@RequestMapping("/comments")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @LoginChecker
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommentResponseDto writeComment(@Login Member member, @RequestBody @Validated CommentDto dto){
        Comment savedComment = commentService.save(dto.getContent(), member, dto.getBoardId());
        CommentResponseDto commentResponseDto = new CommentResponseDto(savedComment);
        return commentResponseDto;
    }

    @LoginChecker
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public GenerateResponseDto updateComment(@PathVariable Long id, @Login Member member, @RequestBody @Validated CommentDto dto){
        validateCommentUpdateAuth(id, member, dto);
        commentService.update(id, dto);
        return GenerateResponseDto.builder().status(200).message("댓글 수정 완료").build();
    }

    @LoginChecker
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public GenerateResponseDto deleteComment(@PathVariable Long id, @Login Member member, @RequestBody @Validated CommentDto dto){
        validateCommentUpdateAuth(id, member, dto);
        commentService.delete(id);
        return GenerateResponseDto.builder().status(200).message("댓글 삭제 완료").build();
    }

    private void validateCommentUpdateAuth(Long id, Member member, CommentDto dto) {
        String commentWriter = commentService.findComment(id).getMember().getName();
        if(!member.getName().equals(dto.getMemberName()) || !member.getName().equals(commentWriter)) {
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "댓글 수정 / 삭제 권한이 없습니다.") {
            };
        }
    }


}

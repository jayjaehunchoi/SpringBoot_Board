package board.bootboard.service;

import board.bootboard.domains.board.Comment;
import board.bootboard.domains.member.Member;
import board.bootboard.web.dto.board.comment.CommentDto;

import java.util.List;

public interface CommentService {

    public Comment save(String content, Member member, Long boardId);
    public List<Comment> findBoardComments(Long commentId, Long boardId);
    public Long update(Long id, CommentDto dto);
    public Long delete(Long id);
    public void deleteByBoardId(Long boardId);
    public Comment findComment(Long id);
}

package board.bootboard.repository;

import board.bootboard.domains.board.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findCommentByCursor(Long commentId, Long boardId);
}

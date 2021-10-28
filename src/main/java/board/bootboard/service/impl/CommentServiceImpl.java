package board.bootboard.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import board.bootboard.domains.board.Board;
import board.bootboard.domains.board.Comment;
import board.bootboard.domains.member.Member;
import board.bootboard.repository.BoardRepository;
import board.bootboard.repository.CommentRepository;
import board.bootboard.service.CommentService;
import board.bootboard.util.exception.PostNotExistException;
import board.bootboard.web.dto.board.comment.CommentDto;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    @Override
    public Comment save(String content, Member member, Long boardId) {
        Board board = findBoard(boardId);
        Comment comment = Comment.builder().content(content).member(member).board(board).build();
        Comment savedComment = commentRepository.save(comment);
        board.addComments();
        return savedComment;
    }

    @Override
    public List<Comment> findBoardComments(Long commentId, Long boardId) {
        return commentRepository.findCommentByCursor(commentId, boardId);
    }

    @Transactional
    @Override
    public Long update(Long id, CommentDto dto) {
        Comment comment = findComment(id);
        comment.update(dto);
        return 1L; // success
    }

    @Override
    public Long delete(Long id) {
        findComment(id);
        commentRepository.deleteById(id);
        return 1L; // success
    }

    @Override
    public void deleteByBoardId(Long boardId) {
        commentRepository.deleteByBoardId(boardId);
    }

    @Override
    public Comment findComment(Long id){
        Comment comment = commentRepository.findById(id).orElse(null);
        if(comment == null){
            throw new PostNotExistException("댓글이 존재하지 않습니다.");
        }
        return comment;
    }
    private Board findBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if(board == null){
            throw new PostNotExistException("게시물이 존재하지 않습니다.");
        }
        return board;
    }
}

package board.bootboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import board.bootboard.domains.board.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    public void deleteByBoardId(Long id);
}

package board.bootboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import board.bootboard.domains.board.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    List<Board> findByTitle(String title);
}

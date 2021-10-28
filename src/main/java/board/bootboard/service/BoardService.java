package board.bootboard.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import board.bootboard.domains.board.Board;
import board.bootboard.web.dto.board.BoardSearchCond;
import board.bootboard.web.dto.board.BoardUpdateDto;
import board.bootboard.web.dto.board.MemberBoardDto;

import java.util.List;

public interface BoardService {
    public Board write(Board board);
    public Board findOne(Long id);
    public Page<MemberBoardDto> findBoardByOffsetPage(BoardSearchCond cond, Pageable pageable);
    public List<MemberBoardDto> findBoardByCursorPage(Long id, int pageSize, BoardSearchCond cond);
    public Board update(Long id, BoardUpdateDto boardUpdateDto);
    public void remove(Long id);


}

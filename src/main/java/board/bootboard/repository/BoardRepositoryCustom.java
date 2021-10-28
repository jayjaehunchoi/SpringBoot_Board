package board.bootboard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import board.bootboard.web.dto.board.BoardSearchCond;
import board.bootboard.web.dto.board.MemberBoardDto;


import java.util.List;

public interface BoardRepositoryCustom {
    List<MemberBoardDto> search(BoardSearchCond cond);
    Page<MemberBoardDto> searchPageSimple(BoardSearchCond cond, Pageable pageable);
    Page<MemberBoardDto> searchPageComplex(BoardSearchCond cond, Pageable pageable);
    List<MemberBoardDto> searchByCursor(Long boardId, int pageSize, BoardSearchCond cond);
}

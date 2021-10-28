package board.bootboard.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import board.bootboard.domains.board.Board;
import board.bootboard.web.dto.board.BoardSearchCond;
import board.bootboard.web.dto.board.BoardUpdateDto;
import board.bootboard.web.dto.board.MemberBoardDto;
import board.bootboard.repository.BoardRepository;
import board.bootboard.service.BoardService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public Board write(Board board) {
        return boardRepository.save(board);
    }

    @Override
    public Board findOne(Long id) {
        Board board = boardRepository.findById(id).orElse(null);
        if(board == null){
            log.error("Board Find Exception");
            throw new IllegalArgumentException("존재하지 않는 게시물입니다.");
        }
        return board;
    }

    @Override
    public Page<MemberBoardDto> findBoardByOffsetPage(BoardSearchCond cond, Pageable pageable) {
        return boardRepository.searchPageComplex(cond, pageable);
    }

    @Override
    public List<MemberBoardDto> findBoardByCursorPage(Long id, int pageSize, BoardSearchCond cond) {
        return boardRepository.searchByCursor(id,pageSize,cond);
    }

    @Transactional
    @Override
    public Board update(Long id, BoardUpdateDto boardUpdateDto) {
        Board board = findOne(id);
        board.updateBoard(boardUpdateDto);
        return board;
    }

    @Override
    public void remove(Long id) {
        Board board = findOne(id);
        boardRepository.delete(board);
        log.info("[게시판 삭제 성공] Id = {}",id);
    }
}

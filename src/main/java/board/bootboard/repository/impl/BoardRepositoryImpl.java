package board.bootboard.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.support.PageableExecutionUtils;
import board.bootboard.domains.board.Board;
import board.bootboard.web.dto.board.BoardSearchCond;
import board.bootboard.web.dto.board.MemberBoardDto;
import board.bootboard.web.dto.board.QMemberBoardDto;

import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import board.bootboard.repository.BoardRepositoryCustom;

import java.util.List;

import static org.springframework.util.StringUtils.*;
import static board.bootboard.domains.board.QBoard.*;
import static board.bootboard.domains.member.QMember.*;

public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MemberBoardDto> search(BoardSearchCond cond) {
        return  queryFactory.select(new QMemberBoardDto(member.id.as("memberId"),
                                    member.name,
                                    board.id.as("boardId"),
                                    board.title,
                                    board.content,
                                    board.likes,
                                    board.createDate))
                            .from(board)
                            .leftJoin(board.member, member)
                            .where(memberNameEq(cond.getName()),
                                    regionEq(cond.getRegion()),
                                    likesGoe(cond.getLikes())
                            )
                            .fetch();
    }

    @Override
    public Page<MemberBoardDto> searchPageSimple(BoardSearchCond cond, Pageable pageable) {

        QueryResults<MemberBoardDto> results = queryFactory.select(new QMemberBoardDto(member.id.as("memberId"),
                                                                    member.name,
                                                                    board.id.as("boardId"),
                                                                    board.title,
                                                                    board.content,
                                                                    board.likes,
                                                                    board.createDate))
                                                            .from(board)
                                                            .leftJoin(board.member, member)
                                                            .where(memberNameEq(cond.getName()),
                                                                    regionEq(cond.getRegion()),
                                                                    likesGoe(cond.getLikes())
                                                            )
                                                            .offset(pageable.getOffset())
                                                            .limit(pageable.getPageSize())
                                                            .fetchResults();
        List<MemberBoardDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MemberBoardDto> searchPageComplex(BoardSearchCond cond, Pageable pageable) {
        List<MemberBoardDto> content = queryFactory.select(new QMemberBoardDto(member.id.as("memberId"),
                                                            member.name,
                                                            board.id.as("boardId"),
                                                            board.title,
                                                            board.content,
                                                            board.likes,
                                                            board.createDate))
                                                    .from(board)
                                                    .leftJoin(board.member, member)
                                                    .where(memberNameEq(cond.getName()),
                                                            regionEq(cond.getRegion()),
                                                            likesGoe(cond.getLikes())
                                                    )
                                                    .orderBy(board.id.desc())
                                                    .offset(pageable.getOffset())
                                                    .limit(pageable.getPageSize())
                                                    .fetch();


        // count 쿼리 최적화, count 먼저 수행하고 0이면 쿼리 수행 안함.
        JPAQuery<Board> countQuery = queryFactory.select(board)
                .from(board)
                .leftJoin(board.member, member)
                .where(memberNameEq(cond.getName()),
                        regionEq(cond.getRegion()),
                        likesGoe(cond.getLikes())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public List<MemberBoardDto> searchByCursor(Long boardId, int pageSize, BoardSearchCond cond) {
        return queryFactory.select(new QMemberBoardDto(member.id.as("memberId"),
                                    member.name,
                                    board.id.as("boardId"),
                                    board.title,
                                    board.content,
                                    board.likes,
                                    board.createDate))
                            .from(board)
                            .leftJoin(board.member, member)
                            .where(memberNameEq(cond.getName()),
                                    regionEq(cond.getRegion()),
                                    likesGoe(cond.getLikes()),
                                    ltBoardId(boardId))
                            .orderBy(board.id.desc())
                            .limit(pageSize)
                            .fetch();
    }

    private BooleanExpression memberNameEq(String name){
        return hasText(name) ? member.name.eq(name) : null;
    }
    private BooleanExpression regionEq(String region){
        return hasText(region) ? board.region.eq(region) : null;
    }
    private BooleanExpression likesGoe(Integer likes){
        return likes != null ? board.likes.goe(likes) : null;
    }
    private BooleanExpression ltBoardId(Long boardId){
        return boardId != null? board.id.lt(boardId) : null; // board.id < 입력 board Id
    }
}

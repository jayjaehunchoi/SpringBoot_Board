package board.bootboard.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import board.bootboard.domains.board.Comment;
import board.bootboard.repository.CommentRepositoryCustom;

import javax.persistence.EntityManager;
import java.util.List;

import static board.bootboard.domains.board.QComment.*;


@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private static final int COMMENT_SIZE = 10;

    private final JPAQueryFactory queryFactory;
    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<Comment> findCommentByCursor(Long commentId, Long boardId) {
        return queryFactory.selectFrom(comment)
                .where(comment.board.id.eq(boardId), goeCommentId(commentId))
                .orderBy(comment.id.asc())
                .limit(COMMENT_SIZE)
                .fetch();
    }
    private BooleanExpression goeCommentId(Long commentId){
        return commentId == null ? null : comment.id.goe(commentId);
    }
}

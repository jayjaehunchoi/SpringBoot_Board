package board.bootboard.domains.board;


import lombok.*;
import board.bootboard.domains.common.BaseTimeEntity;
import board.bootboard.domains.member.Member;
import board.bootboard.web.dto.board.comment.CommentDto;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    @Builder
    public Comment(Board board, Member member, String content) {
        this.board = board;
        this.member = member;
        this.content = content;
    }

    public void update(CommentDto dto){
        content = dto.getContent();
    }
}

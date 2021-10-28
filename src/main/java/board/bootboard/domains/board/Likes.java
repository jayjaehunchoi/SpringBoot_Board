package board.bootboard.domains.board;

import lombok.*;
import board.bootboard.domains.common.BaseTimeEntity;
import board.bootboard.domains.member.Member;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Likes extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Likes(Board board, Member member) {
        this.board = board;
        this.member = member;
    }

    public void clickLikeButton(boolean clicked){
        if(clicked == true){
            board.reduceLikes();
        }else{
            board.addLikes();
        }
    }

}

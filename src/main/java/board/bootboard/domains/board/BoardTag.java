package board.bootboard.domains.board;

import lombok.*;

import javax.persistence.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class BoardTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_tag_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private HashTag hashTag;

    public void createHashTag(HashTag hashTag){
        this.hashTag = hashTag;
        board.getBoardTags().add(this);
    }

    @Builder
    public BoardTag(Board board, HashTag hashTag) {
        this.board = board;
        this.hashTag = hashTag;
    }
}

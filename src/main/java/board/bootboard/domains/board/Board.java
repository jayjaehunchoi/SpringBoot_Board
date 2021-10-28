package board.bootboard.domains.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import board.bootboard.domains.common.BaseTimeEntity;
import board.bootboard.domains.member.Member;
import board.bootboard.web.dto.board.BoardUpdateDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(of = {"id","title","content","region","likes"})
@NoArgsConstructor
@Entity
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String region;

    @Column(name = "likes_count")
    private int likes;
    @Column(name = "comments_count")
    private int comments;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board",fetch = FetchType.LAZY,orphanRemoval = true)
    private List<BoardTag> boardTags = new ArrayList<>();

    @Builder
    public Board(String title, String content, String region, int likes, int comments) {
        this.title = title;
        this.content = content;
        this.region = region;
        this.likes = likes;
        this.comments = comments;
    }

    public void addMember(Member member){
        this.member = member;
        member.getBoards().add(this);
    }

    public void updateBoard(BoardUpdateDto dto){
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.region = dto.getRegion();
    }

    public int addLikes(){
        return ++likes;
    }
    public int reduceLikes(){return --likes;}
    public void addComments(){comments++;}
    public void deleteComments(){comments--;}
}

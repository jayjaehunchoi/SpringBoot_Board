package board.bootboard.web.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Data
public class MemberBoardDto {
    private Long memberId;
    private String name;
    private Long boardId;
    private String title;
    private String content;
    private int likes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime time;

    @QueryProjection
    @Builder
    public MemberBoardDto(Long memberId, String name, Long boardId, String title, String content, int likes, LocalDateTime time) {
        this.memberId = memberId;
        this.name = name;
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.time = time;
    }
}

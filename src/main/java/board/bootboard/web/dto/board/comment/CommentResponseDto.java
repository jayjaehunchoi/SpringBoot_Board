package board.bootboard.web.dto.board.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import board.bootboard.domains.board.Comment;

import java.time.LocalDateTime;

@EqualsAndHashCode
@NoArgsConstructor
@Data
public class CommentResponseDto {

    private Long id;
    private Long boardId;
    private String memberName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime time;
    private String content;

    public CommentResponseDto(final Comment comment){
        this.id = comment.getId();
        this.boardId = comment.getBoard().getId();
        this.memberName = comment.getMember().getName();
        this.time = comment.getModifiedDate();
        this.content = comment.getContent();
    }
}

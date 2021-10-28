package board.bootboard.web.dto.board.comment;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class CommentDto {

    private Long boardId;
    private String memberName;
    @NotBlank(message = "댓글을 입력해주세요.")
    private String content;

}

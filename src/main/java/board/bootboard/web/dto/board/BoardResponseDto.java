package board.bootboard.web.dto.board;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import board.bootboard.domains.board.Board;
import board.bootboard.web.dto.board.comment.CommentResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardResponseDto {

    private Long id;
    private String title;
    private String content;
    private String region;
    private int likes;
    private List<CommentResponseDto> comments;
    private List<String> boardTags;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime time;


    public BoardResponseDto(final Board board, final List<CommentResponseDto> comments){
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.region = board.getRegion();
        this.likes = board.getLikes();
        this.time = board.getModifiedDate();
        this.boardTags = board.getBoardTags().stream().map(tag -> tag.getHashTag().getName()).collect(Collectors.toList());
        this.comments = comments;
    }
}

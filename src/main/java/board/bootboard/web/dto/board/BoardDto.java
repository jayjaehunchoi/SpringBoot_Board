package board.bootboard.web.dto.board;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class BoardDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotBlank(message = "지역을 선택해주세요.")
    private String region;
    private int likes;

    @Builder
    public BoardDto(String title, String content, String region) {
        this.title = title;
        this.content = content;
        this.region = region;
        this.likes = 0;
    }

}

/**
 * {
 *     "title":"제목",
 *     "content":"내용",
 *     "region":"서울"
 * }
 */
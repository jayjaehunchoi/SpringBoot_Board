package board.bootboard.web.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardSearchCond {

    private String name;
    private String region;
    private Integer likes;

}

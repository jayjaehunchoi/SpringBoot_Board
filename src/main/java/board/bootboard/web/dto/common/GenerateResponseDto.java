package board.bootboard.web.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class GenerateResponseDto {

    private int status;
    private String message;

}

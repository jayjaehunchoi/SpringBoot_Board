package board.bootboard.web.dto.member;

import lombok.*;
import board.bootboard.domains.member.Member;

import javax.validation.constraints.*;

@EqualsAndHashCode(exclude = {"password"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberDto {

    @NotBlank(message = "아이디를 입력하세요")
    private String name;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "숫자, 문자 , 특수문자를 포함한 8 ~ 16자 비밀번호를 입력하세요")
    private String password;

    @NotBlank(message = "이메일을 입력하세요")
    @Email(message = "올바른 이메일 형식이 아닙니다.", regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String email;

    @Max(100)
    @NotNull(message = "나이를 입력하세요")
    private Integer age;

    public MemberDto(final Member member) {
        name = member.getName();
        password = member.getPassword();
        email = member.getEmail();
        age = member.getAge();
    }
}

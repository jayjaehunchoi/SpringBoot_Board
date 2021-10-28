package board.bootboard.domains.member;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import board.bootboard.domains.board.Board;
import board.bootboard.web.dto.member.MemberDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Entity
public class Member{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String password;
    private String email;
    private Integer age;

    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();


    @Builder
    public Member(String name, String password, String email ,Integer age) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.age = age;
    }

    public void setEncodedPassword(String password){
        this.password = password;
    }

    public Member(final MemberDto dto){
        this.name = dto.getName();
        this.password = dto.getPassword();
        this.email = dto.getEmail();
        this.age = dto.getAge();
    }
}
/**
 * {
 *     "name":"wogns0108",
 *     "password":"qwer123456!!",
 *     "email":"wogns0108@naver.com",
 *     "age":25
 * }
 */
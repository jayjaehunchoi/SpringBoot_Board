package board.bootboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import board.bootboard.domains.member.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByNameAndPassword(String name, String password);
    Optional<Member> findByName(String name);
    Member findByEmail(String email);
}

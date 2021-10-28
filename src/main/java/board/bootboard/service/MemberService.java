package board.bootboard.service;

import board.bootboard.domains.member.Member;

import java.util.List;

public interface MemberService {
    List<Member> findAllMembers();
    void join(Member member);
    Member checkLoginInfoCorrect(String name, String password);
    Member findMemberById(String name);
}

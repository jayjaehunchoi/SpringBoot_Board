package board.bootboard.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import board.bootboard.domains.member.Member;
import board.bootboard.repository.MemberRepository;
import board.bootboard.service.MemberService;
import board.bootboard.util.exception.DuplicateEmailException;
import board.bootboard.util.exception.DuplicateIdException;
import board.bootboard.util.exception.LoginFailedException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public void join(Member member){
        saveValidator(member);
        member.setEncodedPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
    }

    @Override
    public Member findMemberById(String name) {
        return memberRepository.findByName(name).orElse(null);
    }

    @Override
    public Member checkLoginInfoCorrect(String name, String password) {
        Member findMember = findMemberById(name);
        loginValidator(password, findMember);
        return findMember;
    }

    private void loginValidator(String password, Member findMember) {
        if(findMember == null){
            throw new LoginFailedException("존재하지 않는 아이디 입니다.");
        }
        if(!passwordEncoder.matches(password, findMember.getPassword())){
            throw new LoginFailedException("비밀번호가 틀렸습니다.");
        }
    }

    private void saveValidator(Member member){
        if(findMemberById(member.getName()) != null){
            throw new DuplicateIdException("중복된 아이디 입니다.");
        }
        if(memberRepository.findByEmail(member.getEmail()) != null){
            throw new DuplicateEmailException("중복된 이메일 입니다");
        }
    }
}

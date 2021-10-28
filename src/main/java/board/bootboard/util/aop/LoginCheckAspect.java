package board.bootboard.util.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import board.bootboard.domains.member.Member;
import board.bootboard.util.constant.SessionConst;

import javax.servlet.http.HttpSession;

@Slf4j
@Component
@Aspect
public class LoginCheckAspect {

    @Before("@annotation(board.bootboard.util.annotation.LoginChecker)")
    public void checkLogin(){
        log.info("[AOP 로그인 체커 시작]");
        HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        Member member= (Member)session.getAttribute(SessionConst.SESSION_ID);
        if(member == null){
            log.error("[AOP 로그인 체커 오류]");
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "로그인 하세요."){};
        }
    }
}

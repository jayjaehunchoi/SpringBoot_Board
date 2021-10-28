package board.bootboard.util.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static board.bootboard.util.constant.SessionConst.*;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[PreHandle] 로그인 체커 시작");
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute(SESSION_ID) == null){
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "로그인 하세요") {};
        }
        return true;
    }
}

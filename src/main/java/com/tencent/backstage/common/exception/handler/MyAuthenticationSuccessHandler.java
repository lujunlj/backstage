package com.tencent.backstage.common.exception.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/*
*
* Spring Security提供了一个过滤器来拦截请求并验证用户身份。
* 如果用户身份认证失败，页面就重定向到/login?error，并且页面中会展现相应的错误信息。
* 若用户想要注销登录，可以通过访问@{/logout}请求，在完成注销之后，页面展现相应的成功消息。
* */
//处理登录成功的。
@Component("myAuthenticationSuccessHandler")
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
 
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        //什么都不做的话，那就直接调用父类的方法
        super.onAuthenticationSuccess(request, response, authentication);

        //如果是要跳转到某个页面的
        String url=request.getRequestURI();
        new DefaultRedirectStrategy().sendRedirect(request, response, url);
 
    }
}

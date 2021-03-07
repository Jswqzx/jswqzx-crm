package com.js.Interceptor;

import com.js.settings.domain.User;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class userInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user=(User) request.getSession().getAttribute("user");
        if (user==null){
            response.sendRedirect("http://localhost/CRM/login.jsp");
            return false;
        }else {
            return true;
        }
    }
}


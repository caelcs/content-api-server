package uk.co.caeldev.content.api.features.security;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static uk.co.caeldev.content.api.features.security.OriginsHelper.isValid;

public class CORSInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String origin = request.getHeader("Origin");
        if(isValid(origin))
            response.addHeader("Access-Control-Allow-Origin", origin);
        return true;
    }
}

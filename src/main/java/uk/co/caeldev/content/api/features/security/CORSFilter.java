package uk.co.caeldev.content.api.features.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static uk.co.caeldev.content.api.features.security.OriginsHelper.isValid;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {

    private final static Logger LOGGER = LoggerFactory.getLogger(CORSFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info("Cors Filter Entering");
        final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String originHeader = httpServletRequest.getHeader("Origin");

        if(isValid(originHeader)) {
            LOGGER.info("Adding Header Allow Origin: " + originHeader);
            httpServletResponse.addHeader("Access-Control-Allow-Origin", originHeader);
        }

        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");


        if (!httpServletRequest.getMethod().equals(HttpMethod.OPTIONS.name())) {
            LOGGER.info("Continue filter processing");
            chain.doFilter(request, response);
        } else {
            LOGGER.info("Return OK status for OPTIONS method requests");
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}

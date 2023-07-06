package kr.co.mz.tutorial.jdbc.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화 작업 수행
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        
        if (!isLoggedIn(httpServletRequest)) {
            httpServletResponse.sendRedirect("/login");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // 필터 종료 작업 수행
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        // 세션 등을 활용하여 로그인 여부를 체크하는 로직 작성
        return true;
    }

}

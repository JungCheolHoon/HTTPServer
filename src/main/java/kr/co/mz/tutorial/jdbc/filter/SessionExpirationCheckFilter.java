package kr.co.mz.tutorial.jdbc.filter;

import static kr.co.mz.tutorial.jdbc.Constants.CUSTOMER_IN_SESSION;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kr.co.mz.tutorial.jdbc.db.model.Customer;
import kr.co.mz.tutorial.jdbc.exception.SessionExpiredException;

public class SessionExpirationCheckFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletRequest.setCharacterEncoding("UTF-8");

        var customer = (Customer) httpServletRequest.getSession().getAttribute(CUSTOMER_IN_SESSION);
        if (customer == null) {
            throw new SessionExpiredException();
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // 필터 종료 작업 수행
    }

}

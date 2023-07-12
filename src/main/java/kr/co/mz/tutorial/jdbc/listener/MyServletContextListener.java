package kr.co.mz.tutorial.jdbc.listener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class MyServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // JNDI 이름으로 Context 검색
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            // DataSource 검색
            DataSource dataSource = (DataSource) envContext.lookup("jdbc/hikariDataSource");
            sce.getServletContext().setAttribute("dataSource", dataSource);
        } catch (Exception e) {
            e.printStackTrace();
            // 예외 처리
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}

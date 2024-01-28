# Servlet API 를 구현한 HTTP Server - Jdbc Transactional Tutorial
## Process
#### 1. Servlet - Service - Dao 구조로써 Tomcat9 을 활용한 Multi Thread 기반
#### 2. ServletLinster 를 상속한 클래스를 정의하여 서블릿 컨테이너의 생명주기 구성
#### 3. web.xml 에 Listener 및 Servlet 에 해당하는 라우팅 정보를 정의
## Realizaion From Results 
#### 1. 사용자와 Servlet 서버간에는 HTTP 통신 프로토콜 기반의 TCP 연결을 수행하며 데이터 패킷을 HttpServletRequest 로 캡슐화
#### 2. Tomcat 은 WAS 의 한 종류로써, Servlet Container 와 Serlvet API 를 활용하여 요청과 응답을 처리
#### 3. Servlet Container 란 Servlet 관리하고 처리하는 객체
#### 4. Servlet Container 는 Web.xml 과 Listener 로써 정의된 클래스를 읽어서 설정을 초기화
#### 5. SpringBoot 를 사용하기 이전에는 동적 페이지를 Servlet 에서 생성해서 응답을 반환
#### 6. Spring Boot 에서는 web.xml 을 사용하지 않고, @Controller 어노테이션이 적용된 클래스를 읽어서 리소스 경로를 정의
#### 7. 최근에는 CSR 을 수행하며, 동적인 문서를 반환하기 보다는 객체를 계층형태의 Json 형태로 변환하여 응답을 반환하는 프로세스
#### 8. Transactional 은 try-catch 또는 try-with-resource 구문을 활용하여 commit , rollback , exception 을 처리

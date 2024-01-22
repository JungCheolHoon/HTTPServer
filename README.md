# Servlet 기반 Web Application Server
## Process
#### 1. ServletLinster 를 상속한 객체를 정의
#### 2. web.xml 에 Listener 및 Servlet 에 해당하는 라우팅 정보를 정의
#### 3. Servlet - Service - Dao 구조

## Result
#### 1. Tomcat 은 웹 서버의 한 종류로써, Servlet Container 와 Serlvet 기술을 활용하여 요청과 응답을 처리
#### 2. Servlet Container 란 Servlet 관리하고 처리하는 객체
#### 3. Servlet Container 는 Web.xml 에 정의된 설정을 스캔하여 설정을 초기화
#### 4. Spring Boot 를 사용하기 이전에는 동적 페이지를 Servlet 에서 생성해서 응답을 반환
#### 5. Spring Boot 에서는 web.xml 을 사용하지 않고, @Controller 어노테이션이 적용된 클래스를 스캔하여 메서드 정보와 라우팅 정보를 정의
#### 6. 요즘은 CSR 을 수행하기 때문에, 동적인 문서를 반환하기 보다는 객체를 계층형태의 Json 형식으로 포맷팅하여 응답을 반환하는 프로세스
#### 3. transactional 을 별도로 설정 및 관리 , commit , rollback , exception 처리

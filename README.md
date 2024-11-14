# Java Servlet & JDBC Transaction Tutorial

Tomcat과 Servlet API를 활용한 멀티스레드 기반의 웹 애플리케이션 구현 프로젝트입니다. 서블릿 컨테이너의 생명주기를 이해하고 JDBC 트랜잭션을 직접 다루어 보는 것을 목표로 합니다.

## 📋 목차
- [프로젝트 개요](#프로젝트-개요)
- [시스템 아키텍처](#시스템-아키텍처)
- [주요 기능](#주요-기능)
- [기술 스택](#기술-스택)
- [학습 내용](#학습-내용)

## 프로젝트 개요
이 프로젝트는 Spring Framework를 사용하지 않고 순수 Servlet과 JDBC를 활용하여 웹 애플리케이션을 구현합니다. Servlet Container의 동작 원리와 JDBC 트랜잭션 관리를 실습하여 웹 애플리케이션의 기본 동작 원리를 이해합니다.

## 시스템 아키텍처
### 핵심 컴포넌트
1. **Servlet Container (Tomcat)**
   - 서블릿 생명주기 관리
   - HTTP 요청/응답 처리
   - 멀티스레드 관리

2. **ServletListener**
   - 서블릿 컨테이너 초기화
   - 애플리케이션 생명주기 관리
   - 리소스 초기화

## 주요 기능
- HTTP 요청/응답 처리
- 멀티스레드 기반 요청 처리
- JDBC 트랜잭션 관리
- 동적 웹 페이지 생성
- 데이터베이스 CRUD 작업

## 기술 스택
- **WAS**: Apache Tomcat
- **Servlet API**: Java Servlet
- **Database**: JDBC
- **Configuration**: web.xml

## 학습 내용
### 1. Servlet Container 동작 원리
- ServletListener를 통한 초기화 과정
- 서블릿 생명주기 (init → service → destroy)
- HTTP 요청/응답 처리 과정
  ```java
  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
      // HTTP 요청 처리
  }
  ```

### 2. 트랜잭션 관리
```java
try{
    conn.setAutoCommit(false);
    // 비즈니스 로직 수행
    conn.commit();
} catch (SQLException e) {
    conn.rollback();
    throw e;
}
```

### 3. Spring과의 차이점
1. **설정 방식**
   - Servlet: web.xml 기반 설정
   - Spring: 어노테이션 기반 설정

2. **요청 처리**
   - Servlet: HttpServlet 상속
   - Spring: @Controller 어노테이션

3. **트랜잭션 관리**
   - Servlet: 수동 트랜잭션 관리
   - Spring: @Transactional 어노테이션

### 4. 최신 웹 개발 트렌드와의 비교
1. **페이지 렌더링**
   - JSP: 서버 사이드 렌더링(SSR)
   - 최신: 클라이언트 사이드 렌더링(CSR)

2. **데이터 포맷**
   - 과거: HTML 페이지 전체 전송
   - 현재: JSON 기반 데이터 통신

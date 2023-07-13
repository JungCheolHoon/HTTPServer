<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %><%@ page isErrorPage="true" %><%
  String message = exception.getMessage();

%><html>
<body>
<H2>앗! 오류가 발생하였습니다!<H2>
<h3><%=message%></h3>
</body>
</html>
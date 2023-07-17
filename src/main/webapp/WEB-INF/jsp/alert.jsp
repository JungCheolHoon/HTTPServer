<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="kr.co.mz.tutorial.jdbc.exception.AlertException"%><%@ page isErrorPage="true" %><%
  String redirectUrl = null;
  String message = exception.getMessage();
  if(exception instanceof AlertException) {
    redirectUrl = ((AlertException)exception).getRedirectUrl();
  }
%><script>
alert("<%=message%>");
<%if(redirectUrl.equals("history.back()")){%>
history.back();
<%} else if(redirectUrl != null) {%>
document.location.replace("<%=redirectUrl%>");
<%} else {%>
history.back();
<%}%>

</script>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<table>
<h1 style="color:#84C1FF">Answer</h1>
<c:forEach items="${ts}" var="arr">
<c:if test="${arr.getscore()!=0}">
	<tr><td><h3 style="color:#FFD1A4">${arr.gettitle()}</h3><a href="J:/doc/pdf/${arr.gettitle()}.pdf">open the file</a></td><td>score:${arr.getscore()}</td></tr>
</c:if>
</c:forEach>
</table>
</body>
</html>
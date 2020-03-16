<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<c:choose>
		<c:when test="${delete == true}">
			FILM DELETED ${filmID}
		</c:when>
		<c:otherwise>
			<p>No film found</p>
		</c:otherwise>
</c:choose>

</body>
</html>
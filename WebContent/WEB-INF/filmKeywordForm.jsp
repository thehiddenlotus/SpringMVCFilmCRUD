<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Search by Keyword</title>
</head>
<body>
	<h2>Search films by keyword</h2>
	<form:form action="filmByKeyword.do" method="POST" modelAttribute="user">
		<form:label path="keyword">Keyword:</form:label>
		<form:input path="keyword" />
		<form:errors path="keyword" />
		<br />
		<input type="submit" value="Search" />
	</form:form>
</body>
</html>
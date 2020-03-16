<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Search by ID</title>
</head>
<body>
	<h2>Search films by ID</h2>
	<form:form action="filmByID.do" method="POST">
		<form:label path="id">ID:</form:label>
		<form:input path="id" />
		<form:errors path="id" />
		<br />
		<input type="submit" value="Search" />
	</form:form>
</body>
</html>
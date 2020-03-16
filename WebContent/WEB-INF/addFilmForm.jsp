<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add a film</title>
</head>
<body>
	<h2>Add a film</h2>
	<form:form action="addFilm.do" method="POST" modelAttribute="film" >
		<form:label path="title">Title:</form:label>
		<form:input path="title" />
		<form:errors path="title" />
		<br />
		<form:label path="description">Description:</form:label>
		<form:input path="description" />
		<form:errors path="description" />
		<br />
		<form:label path="releaseYear">Release Year:</form:label>
		<form:input path="releaseYear" />
		<form:errors path="releaseYear" />
		<br />
		<form:label path="languageID">Language: </form:label>
		<form:select path="languageID">
			<form:option value="1">English</form:option>
			<form:option value="2">Italian</form:option>
			<form:option value="3">Japanese</form:option>
			<form:option value="4">Mandarin</form:option>
			<form:option value="5">French</form:option>
			<form:option value="6">German</form:option>
		</form:select>
		<br />
		<form:label path="length">Length:</form:label>
		<form:input path="length" />
		<form:errors path="length" />
		<br />
		<form:label path="rating">Rating:</form:label>
		<form:input path="rating" />
		<form:errors path="rating" />
		<input type="submit" value="Add" />
	</form:form>

</body>
</html>
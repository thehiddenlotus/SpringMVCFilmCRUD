<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Films by keyword</title>
</head>
<body>
	<h3>Films by keyword</h3>
	<c:choose>
		<c:when test="${! empty films}">
			<h2>Films:</h2>
			<table>
				<c:forEach items="${films}" var="film">
					<tr>
						<td>${film.title}</td>
						<td>${film.rating}</td>
						<td>${film.releaseYear}</td>
						<td>${film.rentalRate}</td>
						<td>${film.rentalDuration}</td>
					</tr>
					<tr>
						<td>${film.description }</td>
					</tr>
				</c:forEach>
			</table>
		</c:when>
		<c:otherwise>
			<p>No film found</p>
		</c:otherwise>
	</c:choose>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Film</title>
</head>
<body>
	<c:choose>
		<c:when test="${! empty film}">
			<ul>
				<li>ID: ${film.id}
				<li>Name: ${film.title}</li>
				<li>Description: ${film.description}</li>
				<li>Rating: ${film.rating}</li>
			</ul>
			<form action="updateFilm.do" method="GET">
				<button type="submit" class="btn btn-primary " value="Update Film">Update
					Film</button>
			</form>
			<form action="deleteFilm.do" method="POST">
				<input type="hidden" name="id" value=${ film.id }>
				<button type="submit" class="btn btn-primary " value="Delete Film">Delete
					Film</button>
			</form>
		</c:when>
		<c:otherwise>
			<p>No film found</p>
		</c:otherwise>
	</c:choose>
</body>
</html>
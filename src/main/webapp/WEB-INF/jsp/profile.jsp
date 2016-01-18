<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Профиль</title>
	<link href="<c:url value='../webstyle/css/bootstrap.min.css' />" rel="stylesheet">
	<link href="<c:url value='../webstyle/css/custom-profile.css' />" rel="stylesheet">
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-header">
			<a class="navbar-brand" href="/">Synchronized</a>
			<ul class="nav navbar-nav">
				<li><a href="#">Комната</a></li>
				<li><a href="#">Про нас</a></li>
				<li><a href="#">Контакты</a></li>
				<c:if test="${userName == null }">
					<li><a href="<c:url value='/login'/>">Авторизация</a></li>
					<li><a href="<c:url value='/register'/>">Регистрация</a></li>
				</c:if>
				<c:if test="${userName != null }">
					<li><a style="cursor: default">Привет, ${userName}</a></li>
					<li><a href="<c:url value='/profile/${userName}'/>">Профиль</a></li>
					<li><a href="<c:url value='/logout'/>">Выйти</a></li>
				</c:if>
			</ul>
		</div>
	</nav>
	<div class="container" style="margin-top: 100px">
		<div class="center-block">
			<span style="font-weight: bold; font-size: 25px">Данные профиля ${profile.login}</span>
			<c:if test="${userName ==  profile.login}">
				<a class="btn btn-success navbar-right" href="/profile/edit">Редактировать</a>
			</c:if>
		</div>
		<div><span style="font-weight: bold">Имя пользователя:</span> ${profile.login}</div>
		<div><span style="font-weight: bold">E-mail адрес:</span>
			<c:choose>
				<c:when test="${profile.email != null }">
					${profile.email}
				</c:when>
				<c:otherwise>
					недоступно
				</c:otherwise>
			</c:choose>		
		</div>
		<div>Список комнат:
			<table class="table" style="width: 500px; border: 1px solid; border-radius: 4px; border-color: #eee">
				<thead>
					<tr>
						<th>№</th>
						<th>Название комнаты</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="room" items="${profile.roomsList}" varStatus="loop">
						<tr>
							<td>${loop.index + 1}</td>
							<td class="room-link"><a href="<c:url value='/room/${room.roomId}'/>">${room.title}</a></td>
							<c:if test="${userName ==  profile.login}">
								<td><a href="<c:url value='/delete-room-${room.roomId}'/>" role="button" class="btn btn-primary">Удалить</a></td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
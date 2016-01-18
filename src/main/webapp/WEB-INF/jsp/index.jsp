<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Synchronize Video Service</title>
	<link href="<c:url value='webstyle/css/bootstrap.min.css' />" rel="stylesheet">
	<link href="<c:url value='webstyle/css/custom-index.css' />" rel="stylesheet">
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-header">
			<a class="navbar-brand" href="">Synchronized</a>
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
	<header>
		<div style="text-align:center;padding-top:120px;"><a href="<c:url value='/create-room'/>" id="create-room">Создать комнату</a></div>
	</header>
	<div class="container" style="margin-top: 50px">
		<table class="table">
			<thead>
				<tr>
					<th>№</th>
					<th>Название комнаты</th>
					<th>Создатель</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="room" items="${roomsList}" varStatus="loop">
					<tr>
						<td>${loop.index + 1}</td>
						<td class="room-link"><a href="<c:url value='/room/${room.title}'/>">${room.title}</a></td>
						<td><a href="<c:url value='/profile/${room.userId.login}'/>">${room.userId.login}</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div>${objTest}</div>
		<c:if test="${param.logout != null}">
			<div class="alert alert-success">
				<p>Выход из профиля произведен</p>
			</div>
		</c:if>
	</div>
	<script src="http://code.jquery.com/jquery-latest.js"></script>
	<script src="<c:url value='webstyle/js/index.js' />"></script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>${roomName}</title>
	<link href="<c:url value='../webstyle/css/bootstrap.min.css' />" rel="stylesheet">
	<link href="<c:url value='../webstyle/build/mediaelementplayer.css' />" rel="stylesheet">
	<link href="<c:url value='../webstyle/css/custom-index.css' />" rel="stylesheet">
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
	<video preload="auto" width="640" height="480"  controls="controls">
	<source src="<c:url value='../webstyle/oceans.mp4' />" type='video/mp4'>
	</video>
	<script src="http://code.jquery.com/jquery-latest.js"></script>
	<script src="<c:url value='../webstyle/build/mediaelement-and-player.min.js' />"></script>
	<script>
	$(document).ready(function(){$('video, audio').mediaelementplayer();});
	</script>
</body>
</html>
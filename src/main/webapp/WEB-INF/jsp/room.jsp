<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Room</title>
	<link href="<c:url value='webstyle/css/bootstrap.min.css' />" rel="stylesheet">
	<link href="<c:url value='webstyle/css/custom-room.css' />" rel="stylesheet">
	<link href="<c:url value='webstyle/build/mediaelementplayer.css' />" rel="stylesheet">
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-header">
			<a class="navbar-brand" href="">Synchronized</a>
			<ul class="nav navbar-nav">
				<li><a href="#">Комната</a></li>
				<li><a href="#">Про нас</a></li>
				<li><a href="#">Контакты</a></li>
				<li><a href="<c:url value='/login'/>">Авторизация</a></li>
				<li><a href="<c:url value='/register'/>">Регистрация</a></li>
			</ul>
		</div>
	</nav>
	<video preload="auto" width="640" height="480"  controls="controls">
	<source src="<c:url value='webstyle/oceans.mp4' />" type='video/mp4'>
	</video>
	<script src="http://code.jquery.com/jquery-latest.js"></script>
	<script src="<c:url value='webstyle/build/mediaelement-and-player.min.js' />"></script>
	<script>
	$(document).ready(function(){$('video, audio').mediaelementplayer();});
	</script>
</body>
</html>
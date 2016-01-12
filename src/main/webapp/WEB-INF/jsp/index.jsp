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
			</ul>
		</div>
	</nav>
	<header>
		<div style="text-align:center;padding-top:120px;"><a href="<c:url value='/create-room'/>" id="create-room">Создать комнату</a></div>
	</header>
	<div>${objTest}</div>
</body>
</html>
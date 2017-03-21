<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Редактировать профиль</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<link href="<c:url value='../webstyle/css/custom-edit-profile.css' />" rel="stylesheet">
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-header" style="float: none">
			<a class="navbar-brand" href="/">Synchronized</a>
			<ul class="nav navbar-nav">
				<li><a href="#">Про нас</a></li>
				<li><a href="#">Контакты</a></li>
				<c:if test="${userName == null }">
					<li><a href="<c:url value='/login'/>">Авторизация</a></li>
					<li><a href="<c:url value='/register'/>">Регистрация</a></li>
				</c:if>
				<c:if test="${userName != null }">
					<li><a style="cursor: default">Привет, ${userName}</a></li>
					<li><a href="<c:url value='/profile/${userName}'/>">Профиль</a></li>
				</c:if>
			</ul>
			<ul class="nav navbar-nav navbar-right"  style="padding-right: 15px">
				<c:if test="${userName != null }">
					<li><a href="<c:url value='/logout'/>">Выйти</a></li>
				</c:if>
			</ul>
		</div>
	</nav>
	<div class="container" style="margin-top: 100px">
		<div class="center-block">
			<span style="font-weight: bold; font-size: 25px">Данные профиля ${userName}</span>
		</div>
		<form:form action="/profile/edit" method="POST" modelAttribute="editedUser">
			<div class="form-group">
				<label for="email-edit">Изменить e-mail адрес</label>
				<form:input path="email" type="text" placeholder="E-mail" class="form-control" id="email-edit"/>
				<form:errors path="email"/>
			</div>
			<div class="form-group">
				<label for="password-edit">Ввести новый пароль</label>
				<form:input path="password" type="password" placeholder="Пароль" class="form-control" id="password-edit"/>
				<form:errors path="password"/>
			</div>
			<div class="form-group">
				<label for="password-confirm-edit">Подтвердить введенный пароль</label>
				<form:input path="passwordConfirm" type="password" placeholder="Подтвердить пароль" class="form-control" id="password-confirm-edit"/>
				<form:errors path="passwordConfirm"/>
			</div>
			<form:button type="submit" value="edit" class="btn btn-default">Сохранить изменения</form:button>
		</form:form>
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Регистрация</title>
    <link href="<c:url value='webstyle/css/bootstrap.min.css' />" rel="stylesheet">
    <link href="<c:url value='webstyle/css/custom-register.css' />" rel="stylesheet">
</head>
<body>
<form:form class="form-horizontal" action="register" method="POST" modelAttribute="user">
  <fieldset>
    <div id="legend">
      <legend class="">Регистрация</legend>
    </div>
    <div class="control-group">
      <!-- Username -->
      <label class="control-label"  for="username">Логин</label>
      <div class="controls">
        <form:input path="login" type="text" id="username" name="username" placeholder="" class="input-xlarge"/>
        <form:errors path="login"/>
        <p class="help-block">Логин должен состоять из букв и цифр без пробелов</p>
      </div>
    </div>
 
    <div class="control-group">
      <!-- E-mail -->
      <label class="control-label" for="email">E-mail адрес</label>
      <div class="controls">
        <form:input path="email" type="text" id="email" name="email" placeholder="" class="input-xlarge"/>
        <form:errors path="email"/>
        <p class="help-block">Предоставьте свой E-mail</p>
      </div>
    </div>
 
    <div class="control-group">
      <!-- Password-->
      <label class="control-label" for="password">Пароль</label>
      <div class="controls">
        <form:input path="password" type="password" id="password" name="password" placeholder="" class="input-xlarge"/>
        <form:errors path="password"/>
        <p class="help-block">Длина пароля 7 или больше символов</p>
      </div>
    </div>
 
    <div class="control-group">
      <!-- Password -->
      <label class="control-label"  for="password_confirm">Подтвердите пароль</label>
      <div class="controls">
        <form:input path="passwordConfirm" type="password" id="password_confirm" name="password_confirm" placeholder="" class="input-xlarge"/>
        <form:errors path="passwordConfirm"/>
        <p class="help-block">Пожалуйста подтвердите пароль</p>
      </div>
    </div>
 
    <div class="control-group">
      <!-- Button -->
      <div class="controls">
        <form:button type="submit" value="register" class="btn btn-success">Регистрация</form:button>
      </div>
    </div>
  </fieldset>
</form:form>
</body>
</html>
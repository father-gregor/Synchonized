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
<div class="wrapper">
	<form:form class="form-horizontal" action="register" method="POST" modelAttribute="user">
	  <fieldset>
	    <div id="legend">
	      <legend class="">Регистрация</legend>
	    </div>
	    <div class="control-group">
	      <!-- Username -->
	      <label class="control-label"  for="username">Логин</label>
	      <div class="controls">
	        <form:input path="login" type="text" id="username" name="username" placeholder="" class="form-control"/>
	        <div class="alert alert-danger alert-dismissible" role="alert">
	        <form:errors path="login" element="div" cssClass="alert alert-danger alert-dismissible error"/>
	        <p class="help-block">Логин должен состоять из букв и цифр без пробелов</p>
	      </div>
	    </div>
	 
	    <div class="control-group">
	      <!-- E-mail -->
	      <label class="control-label" for="email">E-mail адрес</label>
	      <div class="controls">
	        <form:input path="email" type="text" id="email" name="email" placeholder="" class="form-control"/>
	       	<c:set var="loginError">
	       		<form:errors path="email"/>
	        </c:set>
	        <c:if test="${loginError != null}">
	        	<h2>PASSED</h2>
	        </c:if>
	        <!--<form:errors path="email" element="div" cssClass="alert alert-danger alert-dismissible error"/>-->
	        <p class="help-block">Напишите свой E-mail</p>
	      </div>
	    </div>
	 
	    <div class="control-group">
	      <!-- Password-->
	      <label class="control-label" for="password">Пароль</label>
	      <div class="controls">
	        <form:input path="password" type="password" id="password" name="password" placeholder="" class="form-control"/>
	        <form:errors path="password" element="div" cssClass="alert alert-danger alert-dismissible error"/>
	        <p class="help-block">Длина пароля 7 или больше символов</p>
	      </div>
	    </div>
	 
	    <div class="control-group">
	      <!-- Password -->
	      <label class="control-label"  for="password_confirm">Подтвердите пароль</label>
	      <div class="controls">
	        <form:input path="passwordConfirm" type="password" id="password_confirm" name="password_confirm" placeholder="" class="form-control"/>
	        <form:errors path="passwordConfirm" element="div" cssClass="alert alert-danger alert-dismissible error"/>
	        <p class="help-block">Пожалуйста подтвердите пароль</p>
	      </div>
	    </div>
	 
	    <div class="control-group">
	      <!-- Button -->
	      <div class="controls">
	        <form:button type="submit" value="register" class="btn btn-lg btn-primary btn-block">Регистрация</form:button>
	      </div>
	    </div>
	  </fieldset>
	</form:form>
</div>
</body>
</html>
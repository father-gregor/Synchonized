<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Авторизация</title>
<link href="<c:url value='webstyle/css/custom-login.css' />" rel="stylesheet">
</head>
<body>
  <div class="wrapper">
  	<c:url var="loginUrl" value="/login" />
    <form action="${loginUrl}" method="POST" class="form-signin">       
    	<h2 class="form-signin-heading">Пожалуйста, авторизируйтесь</h2>
      	<c:if test="${param.error != null}">
	  		<div class="alert alert-danger">
	  			<p>Неверный логин и пароль.</p>
	  		</div>
      	</c:if>
		<input type="text" class="form-control" name="login" placeholder="Логин" required="" autofocus="" />
		<input type="password" class="form-control" name="password" placeholder="Пароль" required=""/> 
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<input type="submit" class="btn btn-lg btn-primary btn-block" value="Войти"/> 
    </form>
  </div>
</body>
</html>
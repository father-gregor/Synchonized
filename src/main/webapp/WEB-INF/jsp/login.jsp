<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Авторизация</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<link href="<c:url value='webstyle/css/custom-login.css' />" rel="stylesheet">
</head>
<body>
  <div class="wrapper">
  	<c:url var="loginUrl" value="/login" />
    <form action="${loginUrl}" method="POST" class="form-signin">       
    	<h3 class="form-signin-heading">Авторизация</h3>
		<input type="text" class="form-control" name="login" placeholder="Логин" required="" autofocus="" />
		<input type="password" class="form-control" name="password" placeholder="Пароль" required=""/> 
		<c:if test="${param.error != null}">
	  		<div class="alert alert-danger alert-dismissible" role="alert">
	  			<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	  			<p>Неверный логин или пароль</p>
	  		</div>
      	</c:if>
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<input type="submit" class="btn btn-lg btn-primary btn-block" value="Войти"/> 
    </form>
  </div>
</body>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script>
	$(".close").click(function() {
		$(this).parent().css("display", "none");
	});
</script>
</html>
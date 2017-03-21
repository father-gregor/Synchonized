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
	      <label class="control-label"  for="username">Логин</label>
	      <div class="controls">
	      	<c:set var="loginError">
	       		<form:errors path="login"/>
	        </c:set>
	        <form:input path="login" type="text" id="username" name="username" placeholder="" class="form-control"/>
	        <c:if test="${loginError != ''}">
		        <div class="alert alert-danger alert-dismissible error" role="alert">
		  			<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		  			<p>${loginError}</p>
		  		</div>
	        </c:if>
	        <p class="help-block">Логин должен состоять из букв и цифр без пробелов</p>
	      </div>
	    </div>
	 
	    <div class="control-group">
	      <label class="control-label" for="email">E-mail адрес</label>
	      <div class="controls">
	        <form:input path="email" type="text" id="email" name="email" placeholder="" class="form-control"/>
	       	<c:set var="emailError">
	       		<form:errors path="email"/>
	        </c:set>
	        <c:if test="${emailError != ''}">
		        <div class="alert alert-danger alert-dismissible" role="alert">
		  			<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		  			<p>${emailError}</p>
		  		</div>
	        </c:if>
	        <p class="help-block">Напишите свой E-mail</p>
	      </div>
	    </div>
	 
	    <div class="control-group">
	      <label class="control-label" for="password">Пароль</label>
	      <div class="controls">
	      	<c:set var="passError">
	       		<form:errors path="password"/>
	        </c:set>	      
	        <form:input path="password" type="password" id="password" name="password" placeholder="" class="form-control"/>
	        <c:if test="${passError != ''}">
		        <div class="alert alert-danger alert-dismissible" role="alert">
		  			<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		  			<p>${passError}</p>
		  		</div>
	        </c:if>
	        <p class="help-block">Длина пароля 7 или больше символов</p>
	      </div>
	    </div>
	 
	    <div class="control-group">
	      <label class="control-label"  for="password_confirm">Подтвердите пароль</label>
	      <div class="controls">
	      	<c:set var="confPassError">
	       		<form:errors path="passwordConfirm"/>
	        </c:set>
	        <form:input path="passwordConfirm" type="password" id="password_confirm" name="password_confirm" placeholder="" class="form-control"/>
	        <c:if test="${confPassError != ''}">
		        <div class="alert alert-danger alert-dismissible" role="alert">
		  			<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		  			<p>${confPassError}</p>
		  		</div>
	        </c:if>
	        <p class="help-block">Пожалуйста подтвердите пароль</p>
	      </div>
	    </div>
	 
	    <div class="control-group">
	      <div class="controls">
	        <form:button type="submit" value="register" class="btn btn-lg btn-primary btn-block">Регистрация</form:button>
	      </div>
	    </div>
	  </fieldset>
	</form:form>
</div>
</body>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script>
	$(".close").click(function() {
		$(this).parent().css("display", "none");
	});
</script>
</html>
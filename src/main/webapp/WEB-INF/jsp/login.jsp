<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Авторизация</title>
<link href="<c:url value='webstyle/css/custom-login.css' />" rel="stylesheet">
</head>
<body>
  <div class="wrapper">
    <form class="form-signin">       
      <h2 class="form-signin-heading">Пожалуйста, авторизируйтесь</h2>
      <input type="text" class="form-control" name="username" placeholder="Логин" required="" autofocus="" />
      <input type="password" class="form-control" name="password" placeholder="Пароль" required=""/>      
      <button class="btn btn-lg btn-primary btn-block" type="submit">Войти</button>   
    </form>
  </div>
</body>
</html>
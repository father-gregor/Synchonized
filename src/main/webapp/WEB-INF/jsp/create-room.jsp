<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Создать комнату</title>
	<link href="<c:url value='webstyle/css/bootstrap.min.css' />" rel="stylesheet">
	<link href="<c:url value='webstyle/css/custom-createroom.css' />" rel="stylesheet">
</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
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
					<li><a h         ref="<c:url value='/profile/${userName}'/>">Профиль</a></li>
					<li><a href="<c:url value='/logout'/>">Выйти</a></li>
				</c:if>
			</ul>
		</div>
	</div>
	<div class="container" style="margin-top: 100px">
		<c:url var="createroomUrl" value="/create-room" />
		<form action="create-room" method="POST" id="form-room" >
			<input type="text" class="form-control input-xlarge" name="title" placeholder="Название комнаты" autofocus="" />
			<input type="submit" class="btn btn-success" value="Создать комнату"/>
		</form>
	</div>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script> 
$(function() {
	$("#form-room").attr("accept-charset", "UTF-8");
	$(".btn").click(function(){
		var fields = $("form").serializeArray();
		var fixedstring;
		$.each(fields, function(i, field) {
			console.log("before " + field.value);
			try{
			    // If the string is UTF-8, this will work and not throw an error.
			    fixedstring=decodeURIComponent($("form").serialize());
			    console.log("after " + fixedstring);
			}catch(e){
			    // If it isn't, an error will be thrown, and we can asume that we have an ISO string.
			    fixedstring=$("form").serialize();
			    console.log("after bad " + fixedstring);
			}
		});
		console.log($("form").serialize());
	});
});
</script>
</body>
</html>
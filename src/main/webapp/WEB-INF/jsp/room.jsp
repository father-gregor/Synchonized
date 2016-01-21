<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>${room.title}</title>
	<link href="<c:url value='../webstyle/css/bootstrap.min.css' />" rel="stylesheet">
	<link href="<c:url value='../webstyle/build/mediaelementplayer.css' />" rel="stylesheet">
	<link href="<c:url value='../webstyle/css/custom-index.css' />" rel="stylesheet">
</head>
<body style="background:#595959; min-width: 547px">
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="fixed-navbar">
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span> 
	        </button>
			<a class="navbar-brand" href="/">Synchronized</a>
		</div>
		<div class="collapse navbar-collapse" id="fixed-navbar">
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
					<li><a href="<c:url value='/profile/${userName}'/>">Профиль</a></li>
					<li><a href="<c:url value='/logout'/>">Выйти</a></li>
				</c:if>
			</ul>
		</div>
	</nav>
	<div class="container" style="margin-top: 100px; margin-bottom: 40px">
		<div class="video-wrapper no-svg">
			<video preload="none" src="" class="player-upload" width="640" height="360" style="width:100%;height:100%"  controls="controls">
				<source id="video-source" type="video/webm">
			</video>
			<video preload="none" class="player-youtube" width="640" height="360" style="width:100%;height:100%; display:none"  controls="controls">
				<source id="video-source" src="" type="video/youtube">
			</video>
		</div>
		<div style="width:100%; height: 500px; background: white">
			<button type="button" id="show-tabs" class="btn btn-success">Добавить видео</button>
			<div style="padding: 20px;width: 700px" id="div-tabs">
				<ul class="nav nav-tabs"  id="upload-tabs">
					<li class="active"><a href="#your-file">Свой файл</a></li>
					<li><a href="#youtube-url">Ссылка на Youtube</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="your-file">
						<form:form action="/playlist/add-upvideo-${roomId}" method="POST" modelAttribute="videoObj" enctype="multipart/form-data">
							<div class="form-group">
								<label for="upload-input">Загрузить файл</label>
								<form:input type="file" accept="video/*" path="file" id="upload-input" class="form-control"/>
								<form:errors path="file"/>
							</div>
							<div class="form-group">
								<label for="upload-input">Название видео</label>
								<form:input type="text" path="title" id="title-input" class="form-control"/>
								<form:errors path="title"/>
							</div>
							<form:button type="submit" value="upload-video" class="btn btn-default">Добавить в Playlist</form:button>
						</form:form>
					</div>
					<div class="tab-pane" id="youtube-url">
						<form:form action="/playlist/add-youtube-${roomId}" method="POST" modelAttribute="videoObj">
							<div class="form-group">
								<label for="youtube-input">Ссылка на Youtube</label>
								<form:input type="text" path="url" id="youtube-input" class="form-control"/>
								<form:errors path="url"/>
							</div>
							<div class="form-group">
								<label for="upload-input">Название видео</label>
								<form:input type="text" path="title" id="title-input" class="form-control"/>
								<form:errors path="title"/>
							</div>
							<form:button type="submit" value="upload-video" class="btn btn-default">Добавить в Playlist</form:button>
						</form:form>
					</div>
				</div>
			</div>
			<div style="padding: 15px;width: 700px">
				<p class="text-center" style="font-size: 30px;font-weight: bold">Playlist</p>
				<ul class="list-group" id="${roomId}">
					<c:forEach var="video" items="${room.videosList}" varStatus="loop">
						<c:choose>
							<c:when test="${video.type == 'upvideo'}">
								<li title="/videos/${roomId}/${video.url}" class="list-group-item playlist-li ${video.type}" id="video${loop.index + 1}">
									${video.title} (${video.url})
									<div id="${video.videoId}" style="display: none"></div>
								</li>
							</c:when>
							<c:when test="${video.type == 'youtube'}">
								<li title="${video.url}" class="list-group-item playlist-li ${video.type}" id="video${loop.index + 1}">
									${video.title} (YouTube)
									<div id="${video.videoId}" style="display: none"></div>
								</li>
							</c:when>
						</c:choose>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
	<script src="http://code.jquery.com/jquery-latest.js"></script>
	<script src="<c:url value='../webstyle/js/bootstrap.min.js' />"></script>
	<script src="<c:url value='../webstyle/build/mediaelement-and-player.min.js' />"></script>
	<script src="<c:url value='../webstyle/js/room.js' />"></script>
</body>
</html>
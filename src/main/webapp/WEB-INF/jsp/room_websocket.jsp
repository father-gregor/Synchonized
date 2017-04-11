<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>${room.title}</title>
	<link href="http://vjs.zencdn.net/5.19.1/video-js.css" rel="stylesheet">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<link href="<c:url value='../webstyle/build/mediaelementplayer.css' />" rel="stylesheet">
	<link href="<c:url value='../webstyle/css/custom-room.css' />" rel="stylesheet">
</head>
<body style="background:#595959; min-width: 547px">
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-header">
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
			<ul class="nav navbar-nav navbar-right" >
				<c:if test="${userName != null }">
					<li><a href="<c:url value='/logout'/>">Выйти</a></li>
				</c:if>
			</ul>
		</div>
	</nav>
	<div class="container" style="margin-top: 100px; margin-bottom: 40px">
		<div class="video-wrapper no-svg" style="position:inherit">
			<video class="video-js vjs-default-skin player-upload" id="room-player" width="640" height="400" style="width:100%">
			</video>
		</div>
		<div style="width:100%; height: 500px; background: white">
			<div style="padding: 20px;width: 700px; margin: auto" id="div-tabs">
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
			<div style="padding: 15px;width: 700px; margin: auto">
				<p class="text-center" style="font-size: 30px;font-weight: bold">Playlist</p>
				<button id="sendid">PRESS</button>
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
	<script src="http://vjs.zencdn.net/5.19.1/video.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/videojs-youtube/2.3.2/Youtube.min.js"></script>
	<script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js' />"></script>
	<script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.2/sockjs.min.js'/>"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
	<script src="<c:url value='../webstyle/js/room_websocket.js' />"></script>
</body>
</html>
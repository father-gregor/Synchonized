
$(function() {
	var PLAYER_TYPE = {
			UPVIDEO: 0,
			YOUTUBE: 1
	}
	var stompClient = null;
	var ytLoaded = $.Deferred(),
		upvideoLoaded = $.Deferred();
	var playlist = null;
	var currentVideo = {
			id: -1
	};
	var roomId = $("ul.list-group").attr("id");
	var setVideoTimerId = null;
	getVideoList();
	function send() {
		console.log("SENDED MESSAGE");
		console.log(player.currentTime());
		stompClient.send("/app/timecenter/" + roomId, {}, JSON.stringify({roomId: roomId, videoId: currentVideo.id, currTime: player.currentTime()}))
	}
	function connect() {
		var socket = new SockJS("/videoroom");
		stompClient = Stomp.over(socket);
		console.log("First");
		stompClient.connect({roomId: roomId}, function(frame) {
			console.log("Connected: " + frame);
			initStompSubscribe();
		});
	}
	function initStompSubscribe() {
		if(stompClient !== null && playlist !== null) {
			stompClient.subscribe("/user/queue/timecenter/" + roomId +"/asktime", function(res) {
				console.log("RECEIVED");
				var video = JSON.parse(res.body);
				if(currentVideo.id === parseInt(video.videoId)) {
					setCurrentTime(video.videoId, video.currTime);
				}
			});
			stompClient.subscribe("/topic/timecenter/" + roomId + "/reporttime", function(res) {

			});
			stompClient.subscribe("/user/queue/timecenter/" + roomId + "/gettime", function(res) {
				console.log("Received time - " + res.body);
				var video = JSON.parse(res.body);
				if(currentVideo.id === video.videoId) {
					player.currentTime(video.currTime);
				}
				//console.log("Time object - " + JSON.parser(res.body));
			});
			stompClient.subscribe("/topic/newvideo/" + roomId, function(res) {
				console.log(res.body);
				var video = JSON.parse(res.body);
				playlist.push(video);
				var url = "";
				if(video.type === "upvideo") {
					url = "/videos/" + roomId + "/" + video.url;
				} else if(video.type === "youtube") {
					url = video.url;
				}
				$(".playlist-ul").append(
						$("<li>").attr("title", url).attr("class", "list-group-item playlist-li " + video.type)
						.attr("id", "video" + video.videoId).append(video.title)
				);
			});
			stompClient.subscribe("/topic/alive", function(res) {
				respondAlive();
			})
			getCurrentTime();
		}
	}
	$(window).on('beforeunload', function() {
		stompClient.disconnect(function() {
			console.log("Confirmed Disconnect");
		});
		console.log("Request Disconnect");
	});
	$("#btn-upvideo").click(function() {
		if(typeof FormData === "function") {
			var uploadForm = new FormData($("#form-upvideo"));
			$.ajax( {
				url: "/playlist/16/add-upvideo",
				data: uploadForm,
				dataType: "text",
				processData: false,
				contentType: false,
				type: "POST",
				success: function(data) {
					console.log("Video Uploaded");
					console.log(data);
				},
				error: function(err) {
					console.log("Error while uploading");
					console.log(err.responseText);
				}
			});
		} else {
			$("#form-upvideo").submit();
		}
		return false;
	});
	$("#btn-youtube").click(function() {
		$.ajax( {
			url: "/playlist/16/add-youtube",
			type: "POST",
			data: JSON.stringify({
				"title": $("input#title-input").val(),
				"url": $("input#youtube-input").val()
			}),
			dataType: "json",
			success: function(data) {
				console.log("Video Uploaded");
			},
			error: function(err) {
				console.log("Video upload failed");
			}
		});
	})
	function respondAlive() {
		console.log("Send Alivebeat");
		stompClient.send("/app/alivebeat", {}, roomId);
	}
	function getCurrentTime() {
		console.log("Send Ask");
		var duration = null;
		var index = getIndexByVideoId(currentVideo.id);
		if(playlist[index].type === "upvideo") {
			duration = player.duration();
		} else if(playlist[index].type === "youtube") {
			console.log("DURATION YOUTUBE - " + ytPlayer.getDuration());
			duration = ytPlayer.getDuration();
		}
		if(Number(duration) > 0) {
			stompClient.send("/app/timecenter/" + roomId + "/asktime", {}, JSON.stringify({
				"videoId": currentVideo.id,
				"roomId": roomId,
				"duration": duration
			}));
		} else {
			setTimeout(getCurrentTime, 3000);
		}
	}
	function getVideoList() {
		$.ajax({
			type: "GET",
			url: "/getvideolist-" + roomId,
			success: function(list) {
				console.log(list);
				playlist = list;
				currentVideo.id = playlist[0].videoId;
			},
			error: function(data, status, e) {
				console.log("GETerror: "+data+" status: "+status+" er:"+e);
			}
		});
	}
	connect();
	//// VIDEOJS PLAYER
	var player = videojs("upvideo-player", {
		controls: true
	});
	player.ready(function() {
		console.log("Player's ready");
		upvideoLoaded.resolve();
	});
	//All players initially loaded
	$.when(ytLoaded, upvideoLoaded).done(function() {
		console.log("DEFERRER RESOLVER");
		if(playlist !== null) {
			setCurrentVideo();
		} else {
			setVideoTimerId = setInterval(function() {
				if(playlist !== null) {
					setCurrentVideo();
					clearInterval(setVideoTimerId);
				}
			}, 5000);
		}
	});
	player.on("loadeddata", function() {
		//player.play();
	});
	player.on("canplaythrough", function() {
		//getCurrentTime(currentVideo.id, player.duration());
		//console.log("Duration " + player.duration());
		player.play();
	});
	player.on("ended", function() {
		loadNextVideo();
	});
	function setCurrentVideo() {
		var videoType = "";
		var url = "";
		var index = getIndexByVideoId(currentVideo.id);
		if(playlist[index].type === "upvideo") { //VIDEOJS VIDEO LOAD
			videoType = "video/" + playlist[index].url.split(".").pop();
			url = "/videos/" + roomId + "/" + playlist[index].url;
			player.src({type: videoType, src: url})
			showCurrentPlayer(PLAYER_TYPE.UPVIDEO);
		} else if(playlist[index].type === "youtube") { //YOUTUBE VIDEO LOAD
			url = parseYoutubeLink(playlist[index].url);
			if(url) {
				ytPlayer.loadVideoById(url);
				showCurrentPlayer(PLAYER_TYPE.YOUTUBE);
			}
		}
	}
	function loadNextVideo() {
		var index = getIndexByVideoId(currentVideo.id);
		if(index < playlist.length) {
			$("#video" + currentVideo.id).css({"background-color": "#ffffff", "color": "#000000"});
			currentVideo.id = playlist[++index].videoId;
			setCurrentVideo();
			getCurrentTime();
		}
	}
	function setCurrentTime(videoId, currTime) {
		var index = getIndexByVideoId(videoId);
		console.log("IN SETCURRENTTIME BEGIN");
		if(playlist[index].type === "upvideo") {
			console.log("IN SETCURRENTTIME PLAYER");
			player.currentTime(currTime);
		} else if(playlist[index].type === "youtube") {
			console.log("IN SETCURRENTTIME YOUTUBE");
			ytPlayer.seekTo(currTime, true);
		}
	}
	function getIndexByVideoId(videoId) {
		for(var i = 0; i < playlist.length; i++) {
			if(playlist[i].videoId == videoId)
				return i;
		}
		return -1;
	}
	//////YOUTUBE PLAYER
	var ytPlayer = null;
	window.onYouTubeIframeAPIReady = function() {
		console.log("Start YOUTUBE Main Function");
		ytPlayer = new YT.Player("youtube-player", {
			height: "360",
			width: "640",
			playerVars: {
				"autoplay": 0,
				"controls": 0
			},
			events: {
				"onReady": onYoutubePlayerReady,
				"onStateChange": onYoutubePlayerStateChange
			}
		});
	}
	function onYoutubePlayerReady(e) {
		ytLoaded.resolve();
		console.log("Youtube player loaded");
	}
	function onYoutubePlayerStateChange(e) {
		if(e.data == -1) {
			console.log("PLAY YOUTUBE VIDEO");
			if(initialSubscribe == true) 
				getCurrentTime();
			ytPlayer.playVideo();
		} else if(e.data == YT.PlayerState.ENDED) {
			loadNextVideo();
		}
	}
	function parseYoutubeLink(url) {
		if(url) {
			var regex = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
			var match = url.match(regex);
			if(match && match[2].length == 11) {
				return match[2];
			}
		}
		return null;
	}
	function showCurrentPlayer(playerType) {
		switch(playerType) {
			case PLAYER_TYPE.UPVIDEO:
				$("#upvideo-player").css("display", "block");
				$("#youtube-player").css("display", "none");
				break;
			case PLAYER_TYPE.YOUTUBE:
				$("#youtube-player").css("display", "block");
				$("#upvideo-player").css("display", "none");
				break;
			default:
				break;
		}
		$("#video" + currentVideo.id).css({"background-color": "#b22222", "color": "#ffffff"});
	}
	//window.addEventListener("unload", disconnectWebsocket);
	$("#upload-tabs a").click(function(e) {
		e.preventDefault();
		$(this).tab("show");
	});
});
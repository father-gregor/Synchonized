
//////YOUTUBE PLAYER
var ytPlayer = null;
function onYouTubeIframeAPIReady() {
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
$(function() {
	var PLAYER_TYPE = {
			UPVIDEO: 0,
			YOUTUBE: 1
	}
	var stompClient = null;
	var ytLoaded = $.Deferred(),
		upvideoLoaded = $.Deferred();
	$(window).on('beforeunload', function() {
		stompClient.disconnect(function() {
			console.log("Confirmed Disconnect");
		});
		console.log("Request Disconnect");
		//return 'Your own message goes here...';
	});
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
			stompClient.subscribe("/queue/timecenter/" + roomId +"/asktime", function(res) {
				
			});
			stompClient.subscribe("/topic/timecenter/" + roomId + "/reporttime", function(res) {
				//console.log(res.body);
			});
			stompClient.subscribe("user/queue/timecenter/" + roomId + "/gettime", function(res) {
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
		}
	}
	function getCurrentTime(videoId, success) {
		stompClient.send("/app/timecenter/" + roomId + "/asktime", {}, currentVideo.id);
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
	document.getElementById("sendid").onclick = send;
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
		getCurrentTime(currentVideo.id);
		console.log("Duration " + player.duration());
		player.play();
	});
	player.on("ended", function() {
		var index = getIndexByVideoId(currentVideo.id);
		if(index <= playlist.length - 1) {
			$("#video" + currentVideo.id).css({"background-color": "#ffffff", "color": "#000000"});
			currentVideo.id = playlist[++index].videoId;
			setCurrentVideo();
		}
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
	function getIndexByVideoId(videoId) {
		for(var i = 0; i < playlist.length; i++) {
			if(playlist[i].videoId == videoId)
				return i;
		}
		return -1;
	}
	//////YOUTUBE PLAYER
	function onYoutubePlayerReady(e) {
		ytLoaded.resolve();
		console.log("Youtube player loaded");
	}
	function onYoutubePlayerStateChange(e) {
		if(e.data == -1) {
			ytPlayer.playVideo();
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
				$("#upvideo-player").css("display", "inline");
				$("#youtube-player").css("display", "none");
				break;
			case PLAYER_TYPE.YOUTUBE:
				$("#youtube-player").css("display", "inline");
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
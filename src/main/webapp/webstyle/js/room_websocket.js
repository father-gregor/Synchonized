
$(function() {
	var stompClient = null;
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
		stompClient.connect({}, function(frame) {
			console.log("Connected: " + frame);
			console.log(JSON.stringify(playlist));
			//send();
			initStompSubscribe();
		});
	}
	function initStompSubscribe() {
		if(stompClient !== null && playlist !== null) {
			stompClient.subscribe("/topic/timecenter/" + roomId +"/gettime", function(res) {
				console.log(res.body);
				var video = JSON.parse(res.body);
				if(currentVideo.id === video.videoId) {
					player.currentTime(video.currTime);
				}
				//console.log("Time object - " + JSON.parser(res.body));
			});
			stompClient.subscribe("/topic/timecenter/" + roomId + "/reporttime", function(res) {
				console.log(res.body);
			})
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
			stompClient.subscribe("/topic/currtime", function(res) {
				console.log("Received - " + JSON.parse(res.body).result);
			})
		}
	}
	function getCurrentTime(videoId, success) {
		stompClient.send("/app/timecenter/" + roomId + "/gettime", {}, currentVideo.id);
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
	function disconnect() {
		stompClient.disconnect();
	}
	connect();
	var player = videojs("room-player", {
		controls: true,
		techOrder: ["html5", "youtube", "flash"],
		youtube: { autoplay: 1}
	});
	player.ready(function() {
		console.log("Player's ready");
		if(playlist !== null) {
			//if(playlistObj.videosList[0].)
			setCurrentVideo();
			//player.src({type: "video/youtube", src: "https://www.youtube.com/watch?v=1dONxX9rifs"});
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
		if(playlist[index].type === "upvideo") {
			videoType = "video/" + playlist[index].url.split(".").pop();
			url = "/videos/" + roomId + "/" + playlist[index].url;
		} else if(playlist[index].type === "youtube") {
			videoType = "video/youtube";
			url = playlist[index].url;
		}
		player.src({type: videoType, src: url});
		$("#video" + currentVideo.id).css({"background-color": "#b22222", "color": "#ffffff"});
	}
	function getIndexByVideoId(videoId) {
		for(var i = 0; i < playlist.length; i++) {
			if(playlist[i].videoId == videoId)
				return i;
		}
		return -1;
	}
	$("#upload-tabs a").click(function(e) {
		e.preventDefault();
		$(this).tab("show");
	});
});
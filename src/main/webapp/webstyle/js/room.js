
$(function() {
	var index = 1;
	var firstPlay = true;
	var pausePressed = false;
	var currentPlayer = null;
	var playerYoutube = null;
	var playerUpload = new MediaElementPlayer(".player-upload", {
		success: function(mediaEl, obj) {
			mediaEl.setSrc("");
			mediaEl.addEventListener("ended", function(e) {
				deleteVideoFromList();
				if($("li#video" + index).length > 0) {
					setCurrentPlayer();
				} 
			});
			$("div.player-upload span.mejs-time-total").bind("click", function() {
				pausePressed = false;
				clearTimeout(timeoutId);
				receiveTimeFromServer();
			});
			mediaEl.addEventListener("pause", function() {
				clearTimeout(timeoutId);
				pausePressed = true;
			});
			mediaEl.addEventListener("play", function() {
				if(pausePressed == true) {
					pausePressed = false;
					receiveTimeFromServer();
				}
			});
			mediaEl.addEventListener("timeupdate", function() {
				videoTime = mediaEl.currentTime;
			});
		}
	}); 
	$(".player-youtube").css("display", "none");
	$("div.player-youtube").css("display", "none");
	$("div.player-upload").css("display", "inline-block");
	
	$(window).load(function() {
		setCurrentPlayer();
		connect();
	});
	
	function setCurrentPlayer() {
		var currentEl = $("li#video" + index);
		if(currentEl.hasClass("youtube") == true) {
			$("div.player-upload").css("display", "none");
			$("div.player-youtube").css("display", "inline-block");
			if($(".player-youtube source").attr("src") == "") {
				$(".player-youtube source").attr("src", currentEl.attr("title"));
				createYoutubePlayer();
			} else {
				playerYoutube.setSrc(currentEl.attr("title"));
				playerYoutube.load();
				playerYoutube.play();
				currentPlayer = playerYoutube;
			}
			videoId = currentEl.children().attr("id");
		} else if(currentEl.hasClass("upvideo") == true) {
			videoId = currentEl.children().attr("id");
			$("div.player-youtube").css("display", "none");
			$("div.player-upload").css("display", "inline-block");
			currentPlayer = playerUpload;
			currentPlayer.setSrc(currentEl.attr("title"));
			if(firstPlay == true) {
				receiveTimeFromServer();
				firstPlay = false;
				//sendTimeToServer();
			} else {
				currentPlayer.load();
				currentPlayer.play();
			}
		} else
			videoId = null;
		$("li#video" + (index-1)).css({
			"background": "white",
			"color": "black"
		});
		currentEl.css({
			"background": "#5cb85c",
			"color": "white"
		});
		index++;
	};
	function createYoutubePlayer() {
		playerYoutube = new MediaElementPlayer(".player-youtube", {
			alwaysShowControls: true,
			success: function(mediaEl, obj) {
				mediaEl.addEventListener("canplay", function() {
					currentPlayer = mediaEl;
					if(firstPlay == true) {
						receiveTimeFromServer();
						firstPlay = false;
					}
				});
				mediaEl.addEventListener("timeupdate", function() {
					videoTime = mediaEl.currentTime;
				});
				$("div.player-youtube span.mejs-time-total").bind("click", function() {
					pausePressed = false;
					clearTimeout(timeoutId);
					receiveTimeFromServer();
				});
				mediaEl.addEventListener("pause", function() {
					clearTimeout(timeoutId);
					pausePressed = true;
				});
				mediaEl.addEventListener("play", function() {
					if(pausePressed == true) {
						pausePressed = false;
						receiveTimeFromServer();
					}
				});
				mediaEl.addEventListener("ended", function(e) {
					deleteVideoFromList();
					if($("li#video" + index).length > 0) {
						setCurrentPlayer();
					}
				});
			},
			error: function() {
				console.log("Error youtube");
			}
		});
	}
	var roomId = $("ul.list-group").attr("id");
	var ajaxVideoTime = null;
	var videoId = null;
	var videoTime = null;
	var timeoutId = null;
	function sendTimeToServer() {
		if(videoTime != null) {
			console.log("sendTime");
			$.ajax({
				headers: { 
			        'Accept': 'text/plain;charset=UTF-8',
			        'Content-Type': 'application/json' 
			    },
				type: "POST",
				url: "/sendtime-ajax",
				data: JSON.stringify({
					"roomId": roomId,
					"videoId": videoId,
					"currTime": videoTime
				}),
				dataType: "json",
				success: function(data) {
					
				},
				error: function(data, status, e) {
					
				}
			});
		}
		timeoutId = setTimeout(sendTimeToServer, 500);
	}
	
	function receiveTimeFromServer() {
		$.ajax({
			type: "GET",
			url: "/gettime-ajax-" + videoId,
			success: function(data) {
				currentPlayer.setCurrentTime(data);
				currentPlayer.play();
				sendTimeToServer();
			},
			error: function(data, status, e) {
				console.log("GETerror: "+data+" status: "+status+" er:"+e);
			}
		});
	}
	
	function deleteVideoFromList() {
		$.ajax({
			type: "GET",
			url: "/deletevideo-ajax-" + videoId,
			success: function(data) {
				
			},
			error: function(data, status, e) {
				console.log("GETerror: "+data+" status: "+status+" er:"+e);
			}
		});
	}
	$("#upload-tabs a").click(function(e) {
		e.preventDefault();
		$(this).tab("show");
	});
	
	var stompClient = null; 
    function connect() {
        var socket = new SockJS('/disconn');
		stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
            setConnected(true);
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/disconnectTest', function(calResult){
            	console.log(JSON.parse(calResult.body).result);
            	showResult(JSON.parse(calResult.body).result);
            });
        });
    }
    function disconnect() {
        stompClient.disconnect();
        console.log("Disconnected");
    }
    function sendNum() {
        var num1 = document.getElementById('num1').value;
        var num2 = document.getElementById('num2').value;
        stompClient.send("/calcApp/add", {}, JSON.stringify({
			"roomId": roomId,
			"videoId": videoId,
			"currTime": videoTime
		}));
    }
    function showResult(message) {
        console.log(message);
    }
});
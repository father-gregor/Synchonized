
$(function() {
	var index = 1;
	var firstPlay = true;
	var currentPlayer = null;
	var playerYoutube = null;
	var playerUpload = new MediaElementPlayer(".player-upload", {
		success: function(mediaEl, obj) {
			mediaEl.setSrc("");
			mediaEl.addEventListener("ended", function(e) {
				console.log("Ended 1");
				deleteVideoFromList();
				if($("li#video" + index).length > 0) {
					setCurrentPlayer();
				} 
				console.log("Ended 2");
			});
			mediaEl.addEventListener("timeupdate", function() {
				videoTime = mediaEl.currentTime;
			});
			console.log("Call success upload");
		}
	}); 
	$(".player-youtube").css("display", "none");
	$("div.player-youtube").css("display", "none");
	$("div.player-upload").css("display", "inline-block");
	
	$(window).load(function() {
		console.log("READY987443");
		setCurrentPlayer();
		receiveTimeFromServer();
	});
	
	function setCurrentPlayer() {
		var currentEl = $("li#video" + index);
		console.log(index + " !!!!!!!!!!!!!y " + currentEl.hasClass("youtube"));
		console.log(index + " !!!!!!!!!!!!!up " + currentEl.hasClass("upvideo"));
		if(currentEl.hasClass("youtube") == true) {
			$("div.player-upload").css("display", "none");
			$("div.player-youtube").css("display", "inline-block");
			if($(".player-youtube source").attr("src") == "") {
				console.log("######First Youtube");
				$(".player-youtube source").attr("src", currentEl.attr("title"));
				createYoutubePlayer();
			} else {
				playerYoutube.setSrc(currentEl.attr("title"));
				playerYoutube.load();
				playerYoutube.play();
			}
			currentPlayer = playerYoutube;
			videoId = currentEl.children().attr("id");
			currPlayer = "youtube";
		} else if(currentEl.hasClass("upvideo") == true) {
			videoId = currentEl.children().attr("id");
			$("div.player-youtube").css("display", "none");
			$("div.player-upload").css("display", "inline-block");
			playerUpload.setSrc(currentEl.attr("title"));
			playerUpload.play();
			if(firstPlay == true) {
				receiveTimeFromServer();
				playerUpload.setCurrentTime(ajaxVideoTime);
				firstPlay = false;
				sendTimeToServer();
			}
			currentPlayer = playerUpload;
			currPlayer = "upvideo";
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
		console.log("setCurrentPlayer END");
	};
	function createYoutubePlayer() {
		playerYoutube = new MediaElementPlayer(".player-youtube", {
			alwaysShowControls: true,
			success: function(mediaEl, obj) {
				console.log("Call success youtube");
				mediaEl.addEventListener("canplay", function() {
					console.log("CAN PLAY");
					if(firstPlay == true) {
						receiveTimeFromServer();
						mediaEl.setCurrentTime(ajaxVideoTime);
						firstPlay = false;
						sendTimeToServer();
					}
					mediaEl.play();
				});
				mediaEl.addEventListener("timeupdate", function() {
					videoTime = mediaEl.currentTime;
				});
				mediaEl.addEventListener("seeked", function() {
					console.log("SEEKED");
				});
				mediaEl.addEventListener("seeking", function() {
					console.log("MOVING");
				});
				mediaEl.addEventListener("pause", function() {
					console.log("PAUSED");
				});
				mediaEl.addEventListener("loadedmetadata", function() {
					console.log("LOADED METADATA");
				}); 
				mediaEl.addEventListener("loadeddata", function() {
					console.log("LOADED VIDEO DATA");
				});
				mediaEl.addEventListener("play", function() {
					console.log("PLAY VIDEO");
				});
				mediaEl.addEventListener("playing", function() {
					console.log("PLAY VIDEO");
				});
				mediaEl.addEventListener("ended", function(e) {
					deleteVideoFromList();
					if($("li#video" + index).length > 0) {
						setCurrentPlayer();
					}
				});
				mediaEl.play();
			},
			error: function() {
				console.log("Error youtube");
			}
		});
	}
	var roomId = $("ul.list-group").attr("id");
	var ajaxVideoTime = null;
	var videoId = null;
	var currPlayer = null;
	var videoTime = null;
	function sendTimeToServer() {
		if(videoId != null) {
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
		setTimeout(sendTimeToServer, 500);
	}
	
	function receiveTimeFromServer() {
		$.ajax({
			type: "GET",
			url: "/gettime-ajax-" + videoId,
			success: function(data) {
				ajaxVideoTime = data;
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
});
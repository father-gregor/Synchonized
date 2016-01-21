
$(function() {
	var index = 1;
	var playerYoutube = null;
	var playerUpload = new MediaElementPlayer(".player-upload", {
		success: function(mediaEl, obj) {
			mediaEl.setSrc("");
			mediaEl.addEventListener("ended", function(e) {
				console.log("Ended 1");
				if($("li#video" + index).length > 0)
					setCurrentPlayer();
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
		setTimeout(sendTimeToServer, 5000);
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
			videoId = currentEl.children().attr("id");
			currPlayer = "youtube";
		} else if(currentEl.hasClass("upvideo") == true) {
			videoId = currentEl.children().attr("id");
			$("div.player-youtube").css("display", "none");
			$("div.player-upload").css("display", "inline-block");
			playerUpload.setSrc(currentEl.attr("title"));
			playerUpload.play();
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
					mediaEl.play();
				});
				mediaEl.addEventListener("timeupdate", function() {
					videoTime = mediaEl.currentTime;
				});
				mediaEl.addEventListener("ended", function(e) {
					if($("li#video" + index).length > 0)
						setCurrentPlayer();
				});
				mediaEl.play();
			},
			error: function() {
				console.log("Error youtube");
			}
		});
	}
	var roomId = $("ul.list-group").attr("id");
	var videoId = null;
	var currPlayer = null;
	var videoTime = null;
	function sendTimeToServer() {
		if(videoId != null) {
			var json = null;
			if(currPlayer == "youtube") {
				json = {
					"roomId": roomId,
					"videoId": videoId,
					"currTime": videoTime
				};
			} else if(currPlayer == "upvideo") {
				json = {
					"roomId": roomId,
					"videoId": videoId,
					"currTime": videoTime
				};
			}
			$.ajax({
				headers: { 
			        'Accept': 'text/plain;charset=UTF-8',
			        'Content-Type': 'application/json' 
			    },
				type: "POST",
				url: "/sendtime-ajax",
				data: JSON.stringify(json),
				dataType: "json",
				success: function(data) {
					console.log(data);
				},
				error: function(data, status, e) {
					console.log("error: "+data+" status: "+status+" er:"+e);
				}
			});
		}
		setTimeout(sendTimeToServer, 3000);
	}
	
	$("#upload-tabs a").click(function(e) {
		console.log(playerYoutube.currentTime);
		console.log(playerUpload.currentTime);
		e.preventDefault();
		$(this).tab("show");
	});
});
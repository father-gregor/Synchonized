
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
			console.log("Call success upload");
		}
	}); 
	$(".player-youtube").css("display", "none");
	$("div.player-youtube").css("display", "none");
	$("div.player-upload").css("display", "inline-block");
	$(window).load(function() {
		console.log("READY987443");
		setCurrentPlayer();
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
		} else if(currentEl.hasClass("upvideo") == true) {
			$("div.player-youtube").css("display", "none");
			$("div.player-upload").css("display", "inline-block");
			playerUpload.setSrc(currentEl.attr("title"));
			playerUpload.play();
		}
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
				mediaEl.addEventListener('canplay', function() {
					console.log("CAN PLAY");
					mediaEl.play();
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
	$("#upload-tabs a").click(function(e) {
		e.preventDefault();
		$(this).tab("show");
	});
});
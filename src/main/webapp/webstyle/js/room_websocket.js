
$(function() {
	var player = videojs("room-player", {
		controls: true,
		autoplay: true,
		techOrder: ["html5", "youtube", "flash"]
	});
	player.ready(function() {
		player.src({type: "video/webm", src: "link to file"});
	});
});
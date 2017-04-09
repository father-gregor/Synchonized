
$(function() {
	var stompClient = null;
	function send() {
		stompClient.send("/app/videoroom", {}, JSON.stringify({"result": "Websocket connection established"}))
	}
	function connect() {
		var socket = new SockJS("/app/videoroom");
		stompClient = Stomp.over(socket);
		stompClient.connect({}, function(frame) {
			console.log("Connected: " + frame);
			stompClient.subscribe("/topic/currtime", function(res) {
				console.log(JSON.parse(res.body).result);
			})
		});
		send();
	}
	function disconnect() {
		stompClient.disconnect();
	}
	connect();
	var player = videojs("room-player", {
		controls: true,
		autoplay: true,
		techOrder: ["html5", "youtube", "flash"]
	});
	player.ready(function() {
		player.src({type: "video/webm", src: "link to file"});
	});
});
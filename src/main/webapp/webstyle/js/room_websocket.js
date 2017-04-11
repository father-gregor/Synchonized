
$(function() {
	var stompClient = null;
	var playlistObj = $("#room-model-obj");
	function send() {
		console.log("SENDED MESSAGE");
		stompClient.send("/app/hello", {}, JSON.stringify({"result": "Websocket connection established"}))
	}
	function connect() {
		var socket = new SockJS("/videoroom");
		stompClient = Stomp.over(socket);
		console.log("First");
		stompClient.connect({}, function(frame) {
			console.log("Connected: " + frame);
			console.log(JSON.stringify(playlistObj));
			//send();
			makeSubscribe();
		});
	}
	function makeSubscribe() {
		if(stompClient !== null && playlistObj !== null) {
			stompClient.subscribe("/topic/timecenter/53", function(res) {
				console.log("Time object - " + JSON.parser(res.body));
			});
			
			stompClient.subscribe("/topic/currtime", function(res) {
				console.log("Received - " + JSON.parse(res.body).result);
			})
		}
	}
	document.getElementById("sendid").onclick = send;
	function disconnect() {
		stompClient.disconnect();
	}
	connect();
	var player = videojs("room-player", {
		controls: true,
		techOrder: ["html5", "youtube", "flash"]
	});
	player.ready(function() {
		console.log("Player's ready");
		if(playlistObj !== null) {
			//if(playlistObj.videosList[0].)
			player.src({type: "video/youtube", src: "https://www.youtube.com/watch?v=1dONxX9rifs"});
		}
	});
	
	$("#upload-tabs a").click(function(e) {
		e.preventDefault();
		$(this).tab("show");
	});
});
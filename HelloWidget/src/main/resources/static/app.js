var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
}

function connect() {
    var socket = new SockJS('/websocket-a');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/queue/telemetry', function (telemetry) {
        	showTelemetry(JSON.parse(telemetry.body).content);
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function start() {
    stompClient.send("/app/start", {}, JSON.stringify({'name': ""}));
}

function showTelemetry(message) {
    $("#telemetry tr td").text(message);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();        
    });
    
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#start" ).click(function() { start(); });
});


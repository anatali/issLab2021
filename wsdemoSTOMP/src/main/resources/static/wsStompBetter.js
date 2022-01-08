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
    $("#output").html("");
}

function connect() {
        //var socket  = new SockJS('/stomp-websocket');
        var host     = document.location.host;
        //var pathname = document.location.pathname;
        var addr     = "ws://" + host + "/unibo"  ; //stomp-websocket
        console.log(addr);
    var socket = new WebSocket(addr);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/demoTopic/output', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    console.log("sendName");
    stompClient.send("/demoInput/unibo", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#output").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});


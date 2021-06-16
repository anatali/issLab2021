var stompClient = null;
//alert("app.js")

function setConnected(connected) {
console.log(" %%% app setConnected:" + connected );
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/it-unibo-iss');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        //setConnected(true);
        //SUBSCRIBE to STOMP topic updated by the server
        stompClient.subscribe('/topic/infodisplay', function (msg) {
             //msg: {"content":"..."}
             //alert(msg)
             var jsonMsg = JSON.parse(msg.body).content;
             showMsg( jsonMsg, "infodisplay" );
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

function sendUpdateRequest(){
	console.log(" sendUpdateRequest "  );
    stompClient.send("/app/update", {}, JSON.stringify({'name': 'update' }));
}

function showMsg(message, outputId) {
    console.log(message );
    $("#"+outputId).html( message.replace(/\n/g,"<br/>")  );
    //$("#applmsgintable").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendRequestData(); });
});



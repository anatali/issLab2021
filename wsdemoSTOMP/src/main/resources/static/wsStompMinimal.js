
    const messageWindow   = document.getElementById("messageArea");
    const sendButton      = document.getElementById("send");
    const messageInput    = document.getElementById("inputmessage");

     sendButton.onclick = function (event) {
        sendMessage(messageInput.value);
        messageInput.value = "";
     };

    function sendMessage(message) {
        stompClient.send("/unibo/inputmsg", {}, JSON.stringify({'name': $("#inputmessage").val()}));
        addMessageToWindow("Sent Message: " + message);
    }

    function addMessageToWindow(message) {
        messageWindow.innerHTML += `<div>${message}</div>`
    }

    //var socket =
    connect();


function connect() {
        //var socket  = new SockJS('/stomp-websocket');
        var host     = document.location.host;
        //var pathname = document.location.pathname;
        var addr     = "ws://" +host  + "/stomp-websocket"  ;
        console.log("connect addr="+addr);

        //var addr     = "ws://localhost/stomp-websocket"  ;
    var socket = new WebSocket(addr);

            socket.onopen = function (event) {
                addMessageToWindow("Connected");
            };

            socket.onmessage = function (event) {
                addMessageToWindow(`Got Message: ${event.data}`);
                //alert(`Got Message: ${event.data}`)

            };

    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        //setConnected(true);
        addMessageToWindow("Connected " + frame);
        //console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/output', function (greeting) {
            showAnswer(JSON.parse(greeting.body).content);
        });
    });
}

function showAnswer(message) {
    addMessageToWindow("Answer:" + message);
}
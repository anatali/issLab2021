
    const messageWindow   = document.getElementById("messageArea");
    const sendButton      = document.getElementById("send");
    const messageInput    = document.getElementById("inputmessage");

     sendButton.onclick = function (event) {
        sendMessage(messageInput.value);
        messageInput.value = "";
     };

    function sendMessage(message) {
        //stompClient.send("/unibo/input", {}, JSON.stringify({'name': $("#inputmessage").val()}));
        stompClient.send("/demoInput/unibo", {}, JSON.stringify({'name': message}));
        addMessageToWindow("Sent Message: " + message ); //+ " stompClient=" + stompClient
    }

    function addMessageToWindow(message) {
        messageWindow.innerHTML += `<div>${message}</div>`
    }

    //var socket =
    connect();


function connect() {
        //var socket   = new SockJS('/unibo');  //NON USIAMO SockJs
        var host       = document.location.host;
        //var pathname = document.location.pathname;
        var addr       = "ws://" + host  + "/unibo"  ;
        console.log("connect addr="+addr);
        var socket     = new WebSocket(addr);

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
        stompClient.subscribe('/demoTopic/output', function (greeting) {
            showAnswer(JSON.parse(greeting.body).content);
        });
    });
}

function showAnswer(message) {
    addMessageToWindow("Answer:" + message);
}
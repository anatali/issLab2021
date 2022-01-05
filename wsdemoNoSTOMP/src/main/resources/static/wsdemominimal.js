
    const messageWindow   = document.getElementById("messageArea");
    const sendButton      = document.getElementById("send");
    const messageInput    = document.getElementById("inputmessage");

     sendButton.onclick = function (event) {
        sendMessage(messageInput.value);
        messageInput.value = "";
    };

    function sendMessage(message) {
        socket.send(message);
        addMessageToWindow("Sent Message: " + message);
    }

    function addMessageToWindow(message) {
        messageWindow.innerHTML += `<div>${message}</div>`
    }

    var socket = connect();


    function connect(){
        var socket;
        var host     = document.location.host;
        var pathname =  document.location.pathname;
        var addr     = "ws://" +host  + pathname + "socket"  ;
        alert("connect addr=" + addr  );

        // Assicura che sia aperta un unica connessione
        if(socket !== undefined && socket.readyState !== WebSocket.CLOSED){
             alert("WARNING: Connessione WebSocket già stabilita");
        }
        socket = new WebSocket(addr);

        //socket.binaryType = "arraybuffer";

        socket.onopen = function (event) {
            addMessageToWindow("Connected");
        };

        socket.onmessage = function (event) {
            addMessageToWindow(`Got Message: ${event.data}`);
            //alert(`Got Message: ${event.data}`)

        };
        return socket;
    }//connect



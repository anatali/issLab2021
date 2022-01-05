    const messageWindow   = document.getElementById("messages");
    const imageWindow     = document.getElementById("images");

    const sendButton      = document.getElementById("send");
    const messageInput    = document.getElementById("message");

    const fileInput       = document.getElementById("file");
    const sendImageButton = document.getElementById("sendImage");

    var socket;

    connect();

    sendButton.onclick = function (event) {
        sendMessage(messageInput.value);
        messageInput.value = "";
    };

    sendImageButton.onclick = function (event) { //PointerEvent
        let file = fileInput.files[0];  //file: object File
        sendMessage(file);
        fileInput.value = null;
    };

    function connect(){
        var host     = document.location.host;
        //const socket = new WebSocket("ws://localhost:8085/socket");

        var pathname =  "/"; //document.location.pathname;
        var addr     = "ws://" +host  + pathname + "socket"  ;
        //alert("connect addr=" + addr  ); //ws://localhost:8085/socket
        //console.log(" socket==undefined is " + (socket == undefined) );

        // Assicura che sia aperta un unica connessione
        if(socket !== undefined && socket.readyState !== WebSocket.CLOSED){
            //writeResponse("Connessione WebSocket già stabilita");
            //return;
            console.log("Connessione WebSocket già stabilita");
        }
        socket = new WebSocket(addr);

        socket.binaryType = "arraybuffer";

        socket.onopen = function (event) {
            addMessageToWindow("Connected");
        };

        socket.onmessage = function (event) {
             console.log("onmessage event=" + event.data);
             if (event.data instanceof ArrayBuffer) {
                addMessageToWindow('Got Image:');
                addImageToWindow(event.data);
            } else {
                addMessageToWindow(`Got Message: ${event.data}`);
            }
        };
        //return socket;
    }//connect

    function sendMessage(message) {
        //alert("sendMessage " + message);
        socket.send(message);
        addMessageToWindow("Sent Message: " + message);
    }

    function addMessageToWindow(message) {
        messageWindow.innerHTML += `<div>${message}</div>`
    }

    function addImageToWindow(image) {
        let url = URL.createObjectURL(new Blob([image]));
        imageWindow.innerHTML += `<img src="${url}"/>`
    }

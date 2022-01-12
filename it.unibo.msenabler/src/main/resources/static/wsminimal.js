
    const messageWindow   = document.getElementById("output");
   
 

    function addMessageToWindow(message) {
        messageWindow.innerHTML += `<div>${message}</div>`
    }

    var socket = connect();


    function connect(){
        var host     = document.location.host;
        var pathname =  document.location.pathname;
        var addr     = "ws://" +host  + pathname + "radarsocket"  ;
        alert("connect addr=" + addr  );

        // Assicura che sia aperta un unica connessione
        if(socket !== undefined && socket.readyState !== WebSocket.CLOSED){
             alert("WARNING: Connessione WebSocket già stabilita");
        }
        var socket = new WebSocket(addr);

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


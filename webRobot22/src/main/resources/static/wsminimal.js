/*
wsminimal.js
*/

    var socket;

    function sendMessage(message) {
        var jsonMsg = JSON.stringify( {'name': message});
        socket.send(jsonMsg);
        console.log("Sent Message: " + jsonMsg);
    }

    function connect(){
      var host       =  document.location.host;
        var pathname =  "/"                   //document.location.pathname;
        var addr     = "ws://" +host  + pathname + "socket"  ;
        //alert("connect addr=" + addr   );

        // Assicura che sia aperta un unica connessione
        if(socket !== undefined && socket.readyState !== WebSocket.CLOSED){
             alert("WARNING: Connessione WebSocket già stabilita");
        }
        socket = new WebSocket(addr);

        socket.onopen = function (event) {
            //console.log("Connected to " + addr);
            setMessageToWindow(infoDisplay,"Connected to " + addr);
        };

        socket.onmessage = function (event) {
            console.log("ws-status:" + `${event.data}`);
            console.log(""+`${event.data}`);
            setMessageToWindow(robotDisplay,""+`${event.data}`);
            //alert(`Got Message: ${event.data}`)
        };
    }//connect


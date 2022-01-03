var ws;

function connect() {
    var username = document.getElementById("username").value;

    var host     = document.location.host;
    var pathname = "/it/unibo/websocketDemo/websocket/";//document.location.pathname;
    var addr     = "ws://" +host  + pathname + "chat/" + username;
    console.log("connect addr=" + addr  );
    console.log(" ws==undefined is " + (ws == undefined) );
    if( ws !== undefined ) console.log(" ws.readyState is " + (ws.readyState) );
    // Assicura che sia aperta un unica connessione
    if(ws !== undefined && ws.readyState !== WebSocket.CLOSED){
    writeResponse("Connesione WebSocket già stabilita");
    return;
    }
    ws = new WebSocket(addr);

    ws.onmessage = function(event) {
    var log = document.getElementById("log");
        console.log(event.data);
        var message = JSON.parse(event.data);
        log.innerHTML += message.from + " : " + message.content + "\n";
    };
}

function send() {
    var content = document.getElementById("msg").value;
    var json = JSON.stringify({
        "content":content
    });

    ws.send(json);
}
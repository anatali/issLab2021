var socket;  //set by connect() called by the enduser


function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) { $("#conversation").show(); }
    else { $("#conversation").hide(); }
    $("#output").html("");
    //console.log("setConnected " + connected);
}

function disconnect() {
    setConnected(false);
    console.log("Disconnected");
}



    function connect(){
        var host     = document.location.host;
        var pathname =  "/"; 	//document.location.pathname;
        var addr     = "ws://" + host  + pathname + "socket"  ;
   
        // Assicura che sia aperta un unica connessione
        if(socket !== undefined && socket.readyState !== WebSocket.CLOSED){
             console.log("Connessione WebSocket già  stabilita");
        }
        //console.log(" connect addr" + addr ); //ws://localhost:8085/socket
        socket = new WebSocket(addr);

        socket.binaryType = "arraybuffer";

        socket.onopen = function (event) {
        	setConnected(true);
            addMessageToWindow("Connected");
        };

        socket.onmessage = function (event) {
             //console.log("onmessage event=" + event.data );
             if (event.data instanceof ArrayBuffer) {
                addMessageToWindow('Got Image:');
                addImageToWindow(event.data);
            } else {
                addMessageToWindow(`Got Message: ${event.data}`);
            }
        };
    }//connect

    function sendMessage(message) {
    console.log("sendMessage " + message );
        socket.send(message);
        addMessageToWindow("Sent Message: " + message);
    }
    
    function sendImage(file){
    console.log("sendImage " + file);
        //let file = fileInput.files[0];  //file: object File
        sendMessage(file);
        //fileInput.value = null;    
    }

    function addMessageToWindow(message) {
    	//console.log("addMessageToWindow " + message);
        $("#output").append("<tr><td>" + message + "</td></tr>");
     }

    function addImageToWindow(image) {
        let url = URL.createObjectURL(new Blob([image]));
        $("#output").append("<tr><td>" + `<img src="${url}"/>` + "</td></tr>");
        //imageWindow.innerHTML += `<img src="${url}"/>`
    }

const fileInput = document.getElementById("myfile");
console.log("fileInput="+fileInput.files[0]);

$(function () {
    $("form").on('submit', function (e) { e.preventDefault(); });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#sendmsg" ).click(function() {  sendMessage($("#inputmsg").val()); });
    //$( "#sendImage" ).click(function() {  let f = $("#myfile"); console.log(  f.files ); sendImage(f); });
    $( "#sendImage" ).click(function() {  let f = fileInput.files[0]; console.log(  f  ); sendImage(f); });
});


 /*
    const messageWindow   = document.getElementById("messages");
    const imageWindow     = document.getElementById("images");

    const connectButton   = document.getElementById("connect");

    const sendButton      = document.getElementById("send");
    const messageInput    = document.getElementById("message");

    const fileInput       = document.getElementById("file");
    const sendImageButton = document.getElementById("sendImage");
*/

  /*  
    sendButton.onclick = function (event) {
        sendMessage(messageInput.value);
        messageInput.value = "";
    };
    
 
    connectButton.onclick = function(event){
    console.log("connect onclick:" + event.data );
    	connect();
    }

    sendImageButton.onclick = function (event) { //PointerEvent
        let file = fileInput.files[0];  //file: object File
        sendMessage(file);
        fileInput.value = null;
    };
*/
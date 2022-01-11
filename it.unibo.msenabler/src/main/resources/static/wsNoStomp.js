var socket;		//set by connect() called by the enduser
var sockConnected ;

 
function setConnected(connected) {
	sockConnected = connected;
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    //if (connected) { $("#conversation").show(); } else { $("#conversation").hide(); }
    $("#output").html("");
    console.log("setConnected " + sockConnected);
    
}

function disconnect() {
    setConnected(false);
    addMessageToWindow("Disconnected");
}
 
    function connect(){
        var host       = document.location.host;
        var pathname   =  "/"; 	//document.location.pathname;
        var socketName = "sonarsocket";
        var addr     = "ws://" + host  + pathname + socketName  ;
        //alert( "mysocket=" + mysocket );
        // Assicura che sia aperta un unica connessione
        if(socket !== undefined && socket.readyState !== WebSocket.CLOSED){
             console.log("Connessione WebSocket gia' stabilita");
         }
        console.log(" connect addr" + addr ); //ws://localhost:8081/sonarsocket
        //alert("connect addr=" + addr);
        socket = new WebSocket(addr);

        socket.binaryType = "arraybuffer";

        socket.onopen = function (event) {
        	setConnected(true);
            addMessageToWindow("Connected");
        };
  
        socket.onmessage = function (event) {
             alert("onmessage event=" + event.data );
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
        if( socket == undefined  ) alert("Please connect ..."); //|| ! sockConnected
        else{
            socket.send(message);
            addMessageToWindow("Sent Message: " + message);
        }
    }
     

    function addMessageToWindow(message) {
    	console.log("addMessageToWindow " + message);
    	//alert("addMessageToWindow " + message );
        $("#output").append("<tr><td>" + message + "</td></tr>");
     }

    function addImageToWindow(image) {
        let url = URL.createObjectURL(new Blob([image]));
        $("#output").append("<tr><td>" + `<img src="${url}"/>` + "</td></tr>");
        //imageWindow.innerHTML += `<img src="${url}"/>`
    }

//const fileInput = document.getElementById("myfile");
//console.log("fileInput="+fileInput.files[0]);

/*
The <button> element, when placed in a form, 
will submit the form automatically unless otherwise specified. 
You can use the following 2 strategies:

1 - Use <button type="button"> to override default submission behavior
2 - Use event.preventDefault() in the onSubmit event to prevent form submission

*/
$(function () {
    $("form").on('submit', function (e) {   });    //e.preventDefault();
    $( "#connect" ).click( function() { connect(); });  
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#on" ).click(function() {  });  //console.log("on");
    $( "#off" ).click(function() {  });
    $( "#doLedBlink" ).click(function() {  });
    $( "#stopLedBlink" ).click(function() {  });
    $( "#distance" ).click(function() {  });
//    $( "#sendImage" ).click(function() {  let f = fileInput.files[0]; sendMessage(f); });
});

 
 
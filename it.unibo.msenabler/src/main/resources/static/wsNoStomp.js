 var socket;
 
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
        	//setConnected(true);
            addMessageToWindow("Connected to WebSocket:" + addr + " socket=" + socket);
        };
  
        socket.onmessage = function (event) {
             console.log(" onmessage" + (event.data instanceof ArrayBuffer) );
             //alert("onmessage");
             if (event.data instanceof ArrayBuffer) {
                addMessageToWindow('Got Image:' );
                addImageToWindow(event.data);
            } else {
                //alert(" onmessage data=" + event.data );
                updateSonarData( event.data );

            }
        };     
         
    }//connect
 
 connect();


//Si veda: https://developer.mozilla.org/en-US/docs/Web/API/Element
    
    function updateSonarData(message) {
    	 //console.log("setMessageToWindow " + message + " " + $("#sonarData").innerHTML);
         //$("#sonarData").innerHTML = ""+message  ;
         document.getElementById("sonarData").innerHTML = ""+message  ;
    }

    function addMessageToWindow(message) {
    	//console.log("addMessageToWindow " + message);
        $("#output").append("<tr><td>" + message + "</td></tr>");
     }


const imgArea = document.getElementById('imgphoto');
          
    //Il Raspberry potrebbe inviare immagini ... 
    function addImageToWindow(image) {
        console.log("addImageToWindow " + image);
        let url = URL.createObjectURL(new Blob([image]));
        console.log("addImageToWindow url=" + url);
        //$("#output").append("<tr><td>" + `<img src="${url}"/>` + "</td></tr>");   //OK
        console.log(  imgArea    );
        imgArea.src  = url;
        //document.getElementById('imgphoto').src  = url;  // `<img src="${url}"/>`  ;
        console.log(  imgArea.src  );
        //imageWindow.innerHTML += `<img src="${url}"/>`
    }
    var attempts = 0;
    function sendMessage(message) {
        console.log("sendMessage " + message + " using " + socket);
        //alert("xxx sendMessage");
        if( socket == null  ) alert("Please connect ...");
        else{
            socket.send(message);
            //addMessageToWindow("Sent Message: " + message);
        }
        
        /*
        alert("sendMessage");
            setTimeout( () => {
                if( socket != undefined ){
                     socket.send(message);
                     addMessageToWindow("Sent Message: " + message);
                     attempts= 0;
                }else{
                if( attempts++ < 3)
                    sendMessage(message);
                }
            }, 1000) ;
            */
    }

    function showCurrentPhoto(){
        console.log("showCurrentPhoto curPhoto.jpg "  );

    }




//https://web.dev/read-files/#read-content
function readImage(file) {
  alert(file.name);
  // Check if the file is an image.
  if (file.type && !file.type.startsWith('image/')) {
    alert('File is not an image.', file.type, file);
    return;
  }

  const reader = new FileReader();
  reader.addEventListener('load', (event) => {
        imgArea.src = event.target.result;
    //addImageToWindow(event.target.result);
  });
  reader.readAsDataURL(file);
}//readImage

//https://web.dev/file-system-access/#opening-a-directory-and-enumerating-its-contents
//await Promise.all for file length
const butDir = document.getElementById('dirshow');
butDir.addEventListener('click', async () => {
  const dirHandle = await window.showDirectoryPicker();
  for await (const entry of dirHandle.values()) {
    console.log(entry.kind, entry.name);
  }
});

const fileInput = document.getElementById("myfile");
//console.log("fileInput="+fileInput.files[0]);


/*
The <button> element, when placed in a form, 
will submit the form automatically unless otherwise specified. 
You can use the following 2 strategies:

1 - Use <button type="button"> to override default submission behavior
2 - Use event.preventDefault() in the onSubmit event to prevent form submission

*/
$(function () {
    //$( "form").on('submit', function (e) { e.preventDefault();  });    
    $( "#connect" ).click( function() { connect(); });  
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#on" ).click(function() {  });  //console.log("on");
    $( "#off" ).click(function() {  });
    $( "#doLedBlink" ).click(function() {  });
    $( "#stopLedBlink" ).click(function() {  });
    $( "#distance" ).click(function() {  });
    //$( "#distance" ).click(function() {  });

    $( "#sendImage" ).click(function() {  let f = fileInput.files[0];  sendMessage(f); }); //readImage(f);

});




 
/* 

//OLD PART from wsdemoNoSTOMP

var socket = connect();		//set by connect() called by the enduser

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

    function sendMessage(message) {
        console.log("sendMessage " + message );
        if( socket == undefined  ) alert("Please connect ..."); //|| ! sockConnected
        else{
            socket.send(message);
            addMessageToWindow("Sent Message: " + message);
        }
    }
    


 //const fileInput = document.getElementById("myfile");
//console.log("fileInput="+fileInput.files[0]);
    
*/ 
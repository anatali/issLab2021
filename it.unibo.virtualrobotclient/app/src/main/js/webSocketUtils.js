/*
webSocketUtils
*/

const wspath    = ("ws://localhost:8091" )
const conn8091  = new WebSocket(wspath);

//alert(WebSocket)
conn8091.addEventListener('open', (conn) => {
  console.log('webSocketUtils | connected ' + conn);
  //doJob()
  cmdRobot( "moveForward", 400, conn8091 )   //see wsclientBoundaryLogic
});
conn8091.addEventListener('close', () => {
  //conn8091.send('webSocketUtils | Robot WebGui is closed');
});
conn8091.addEventListener('message', event => {
  console.log( event )
  var message = event.data;
  document.getElementById("display").innerHTML += message +"\n";
  walkBoundary( JSON.parse(message), conn8091)
});

/*
    function doMove(move, time) {
        const moveJson = '{"robotmove":"'+ move +'"'+', "time":'+time+'}'
        console.log("doMove moveJson:" + moveJson);
        if (conn8091) { conn8091.send(moveJson) }
    }

function doJob(){
     doMove( "moveForward", 600 )
     setTimeout( () => {
        doMove( "moveBackward", 600 );
        console.log("now workign as an observer  ... " ); 
	 }, 800 )
}
*/

/*

//IMPORTANT: https://www.pegaxchange.com/2018/03/23/websocket-client/

/*
Called by a click on rsocket|lsocket|... Communicates with webguiServer.js by using the conn8091
*/
function requestTodoTheMove(move){
	console.log("webSocketUtils | requestTodoTheMove in webSocketUtils/utils " + move);
	conn8091.send( move );     //towards webguiServer.js
}

function openGui(gui){
	console.log("webSocketUtils | openGui in webSocketUtils/utils " + gui);
	conn8091.send( move );     //towards webguiServer.js
}

//node webSocketUtils

/*
webSocketUtils (to be loaded within the virtualrobotcontrol web-page index.html)
*/
const wspath  = ("ws://"+location.host)
//const wspath  = ("ws://localhost:3001';
const socket  = new WebSocket(wspath);
//alert(WebSocket)
socket.addEventListener('connection', () => {
  socket.send('webSocketUtils | Robot WebGui is on');
});
socket.addEventListener('close', () => {
  socket.send('webSocketUtils | Robot WebGui is closed');
});
socket.addEventListener('message', event => {
  var message = event.data;
  document.getElementById("robotDisplay").innerHTML= message;
});


/*

//IMPORTANT: https://www.pegaxchange.com/2018/03/23/websocket-client/

/*
Called by a click on rsocket|lsocket|... Communicates with webguiServer.js by using the socket
*/
function requestTodoTheMove(move){
	console.log("webSocketUtils | requestTodoTheMove in webSocketUtils/utils " + move);
	socket.send( move );     //towards webguiServer.js
}

function openGui(gui){
	console.log("webSocketUtils | openGui in webSocketUtils/utils " + gui);
	socket.send( move );     //towards webguiServer.js
}

//node webSocketUtils

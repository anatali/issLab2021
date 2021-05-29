const express = require('express');
const app     = express();
const http    = require('http');
const server  = http.createServer(app);

/* ADDED for socket.io */
const { Server } = require("socket.io");
const io         = new Server(server);  //A server that integrates with (or mounts on) the Node.JS HTTP

/* ADDED for CoAP */
const coap       = require("./uniboSupports/coapClientToResourceModel"); 

app.get('/', (req, res) => {
  //res.send('<h1>Hello world</h1>');
  res.sendFile(__dirname + '/sonarGui.html'); //route handler to use sendFile instead
});

/*
GOALS: 
	1) modify the resource using CoAp
	2) interact with the qak model sonarresource
*/
//app.post("/start", function(req, res,next) { coap.coapPut(cmd); });	
//app.post("/stop",  function(req, res,next) { coap.coapPut(cmd); });	



/* ADDED for socket.io */
io.on('connection', (socket) => {
  console.log('user connected');
  
  socket.on('disconnect', () => {
    console.log('user disconnected');
  });
  
  socket.on('sonarvalue', (msg) => {
    console.log('message: ' + msg);
    var event = "msg(sonarrobot,dispatch,gui,sonarresource,sonar("+ msg + "), 1)";
    console.log('event: ' + event);
    coap.coapPut( event );
    
    //io.emit('chat message', msg);   //send to all, including the sender
  }); 
  
});

/*
MAIN
*/
server.listen(3000, () => {
  console.log('listening on *:3000');
});

//localhost:3000

/*
So far in index.js we’re calling res.send and passing it a string of HTML. 
Our code would look very confusing if we just placed our entire application’s HTML there, 
so instead we’re going to create a sonarGui.html file and serve that instead.
*/
//See  https://socket.io/get-started/chat
const express = require('express');
const app     = express();
const http    = require('http');
const server  = http.createServer(app);

/* ADDED for socket.io */
const { Server } = require("socket.io");
const io         = new Server(server);  //A server that integrates with (or mounts on) the Node.JS HTTP

app.get('/', (req, res) => {
  //res.send('<h1>Hello world</h1>');
  res.sendFile(__dirname + '/chat.html'); //route handler to use sendFile instead
});


/* ADDED for socket.io */
io.on('connection', (socket) => {
  console.log('user connected');
  
  socket.on('disconnect', () => {
    console.log('user disconnected');
  });
  
  socket.on('chat message', (msg) => {
    console.log('message: ' + msg);
    io.emit('chat message', msg);   //send to all, including the sender
    /*
    If you want to send a message to everyone except for a certain emitting socket, 
    we have the broadcast flag for emitting from that socket:
    	socket.broadcast.emit('hi');
    */
  }); 
  
});

/*
MAIN
*/
server.listen(3500, () => {
  console.log('listening on *:3500');
});

//localhost:3500

/*
So far in index.js we’re calling res.send and passing it a string of HTML. 
Our code would look very confusing if we just placed our entire application’s HTML there, 
so instead we’re going to create a sonarGui.html file and serve that instead.
*/
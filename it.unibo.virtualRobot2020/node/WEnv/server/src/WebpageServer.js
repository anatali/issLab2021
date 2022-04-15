/*
==========================================================================
WebpageServer.js
==========================================================================
*/

const app          = require('express')()
const express      = require('express')
const hhtpSrv      = require('http').Server(app)
const sceneSocket  = require('socket.io')(hhtpSrv)     //interaction with WebGLScene
const WebSocket    = require('ws');                    //interaction with external clients

const sockets       = {}    //interaction with WebGLScene
const wssockets     = {}    //interaction with clients
let socketIndex     = -1
let wssocketIndex   = -1
const serverPort    = 8090

//STATE variables used by cmd handling on wssockets (see initWs)
var alreadyConnected = false
let runningMovesIndex   = -1
const moveMap   = new Map();
var postResult  = null

app.use(express.static('./../../WebGLScene'))

/*
-----------------------------------------------------------------------------
Defines how to handle GET from browser and from external controls
-----------------------------------------------------------------------------
*/
    app.get('/', (req, res) => {
	    console.log("WebpageServer | GET socketIndex="+socketIndex + " alreadyConnected =" + alreadyConnected )
        if( ! alreadyConnected ){
            alreadyConnected = true;
            res.sendFile('indexOk.html', { root: './../../WebGLScene' })
	     }else{
		    res.sendFile('indexNoControl.html', { root: './../../WebGLScene' })
	     }
    }); //app.get

/*
-----------------------------------------------------------------------------
Defines how to handle POST from browser and from external controls
-----------------------------------------------------------------------------
*/	//USING POST : by AN Jan 2021
    app.post("/api/move", function(req, res,next)  {
	    var data = ""
	    req.on('data', function (chunk) { data += chunk; }); //accumulate data sent by POST
            req.on('end', function () {	//elaborate data received
			//{ robotmove: move, time:duration } - robotmove: turnLeft | turnRight | ...
			console.log('POSTTT /api/move data ' + data + " "  + moveMap[runningMovesIndex] );
			var jsonData = JSON.parse(data)
     		var moveTodo = jsonData.robotmove
     		var duration = jsonData.time
     		if( moveTodo=="alarm"){ //do the halt move
 	            execMoveOnAllConnectedScenes(moveTodo, duration) //JULY2021 April 2002
                if( res != null ){
                    res.writeHead(200, { 'Content-Type': 'text/json' });
                    res.statusCode=200
                    var answer       = { 'endmove' : 'true' , 'move' : 'halt' }  //JSON obj
                    const answerJson = JSON.stringify(answer)
                    res.write( answerJson  );
                    res.end();
                }
                return
            }//the move is not alarm
			//console.log(" runningMovesIndex=" + runningMovesIndex + " " + moveMap[runningMovesIndex] );
			if( moveMap[runningMovesIndex] != undefined && moveMap[runningMovesIndex] != "interrupted" ){
			//the move DOES NOT 'interrupt' a move activated in asynch way
	            const answer  = { 'endmove' : "notallowed" , 'move' : moveTodo }
	            updateCallers( JSON.stringify(answer) )
                if( res != null ){
                    res.writeHead(200, { 'Content-Type': 'text/json' });
                    res.statusCode=200
                    const answerJson = JSON.stringify(answer)
                    res.write( answerJson  );
                    res.end();
                }
			} else{
	            console.log('$$$ WebpageServer doMove | ' + moveTodo + " duration=" + duration  );
                postResult = res  //MEMO that a POST is running. See
	            execMoveOnAllConnectedScenes(moveTodo, duration)
			}
  	   });
	}); //app.post


//Updates the mirrors
function execMoveOnAllConnectedScenes(moveTodo, moveTime){
    if( moveTodo != "alarm") {
        runningMovesIndex++
        console.log('$$$ WebpageServer | execMoveOnAllConnectedScenes '  + moveTodo + " index=" + runningMovesIndex);
        moveMap.set(runningMovesIndex, moveTodo)
    }
	Object.keys(sockets).forEach( key => sockets[key].emit(moveTodo, moveTime, runningMovesIndex) );
}

//Updates the controls and the observers (Jan 2021)
function updateCallers(msgJson){
 	Object.keys(wssockets).forEach( key => {
        console.log("WebpageServer | updateCallers key="  + key + " msgJson=" + msgJson);
        wssockets[key].send( msgJson )
	} )
}

/*
-------------------------------------------------------------------------------------
Interact with clients over ws (controls that send commands or observers) Jan 2021
-------------------------------------------------------------------------------------
*/


function initWs(){
const wsServer  = new WebSocket.Server( { port: 8091 }  );   // { server: app.listen(8091) }
console.log("       $$$ WebpageServer | initWs wsServer=" + wsServer)

wsServer.on('connection', (ws) => {
  wssocketIndex++
  console.log("     $$$ WebpageServer wssocket | CLIENT CONNECTED wssocketIndex=" + wssocketIndex)
  const key      = wssocketIndex
  wssockets[key] = ws

  ws.on('message', msg => {
	var moveTodo = JSON.parse(msg).robotmove
	var duration = JSON.parse(msg).time
    var curMove = moveMap.get(runningMovesIndex)
    console.log("       $$$ WebpageServer wssocket=" + wssocketIndex   + " receives: "
        + msg  + " curMove="+ curMove )
	if( curMove != undefined && curMove != "interrupted" && moveTodo != "alarm"){
        console.log("$$$ WebpageServer ws | SORRY: cmd " + msg + " NOT POSSIBLE, since I'm running:" + curMove)
        const info     = { 'endmove' : false, 'move': moveTodo+"_notallowed" }
        updateCallers( JSON.stringify(info) )
	    return
	}else  //the alarm move could also be sent via HTTP
 	 if( curMove != undefined && curMove != "interrupted" && moveTodo == "alarm" ){
        moveMap.set(runningMovesIndex,"interrupted")
	    execMoveOnAllConnectedScenes(moveTodo, duration)
        const info     = { 'endmove' : false, 'move': curMove+"_halted" }
        updateCallers( JSON.stringify(info) )
	    return
	}else{
	    console.log('AAA $$$ WebpageServer doMoveAsynch | ' + moveTodo + " duration=" + duration )
        execMoveOnAllConnectedScenes(moveTodo, duration)
	}
  });

  ws.onerror = (error) => {
	  console.log("     $$$ WebpageServer wssocket | error: ${error}")
	  delete wssockets[key];
	  wssocketIndex--
	  console.log( "        $$$ WebpageServer wssocket | disconnect wssocketIndex=" +  wssocketIndex )
  }

  ws.on('close', ()=>{
	  delete wssockets[key];
	  wssocketIndex--
	  console.log( "        $$$ WebpageServer wssocket | disconnect wssocketIndex=" +  wssocketIndex )
  })
}); //wsServer.on('connection' ...
}//initWs


/*
-------------------------------------------------------------------------------------
Interact with the MASTER (the mirrors do not send any info)
-------------------------------------------------------------------------------------
*/
function sceneSocketInfoHandler() {
	console.log("WebpageServer sceneSocketInfoHandler |  socketIndex="+socketIndex)
    sceneSocket.on('connection', socket => {
        socketIndex++
        console.log("WebpageServer sceneSocketInfoHandler  | connection socketIndex="+socketIndex)
        const key    = socketIndex
        sockets[key] = socket
        if( socketIndex == 0) console.log("WebpageServer sceneSocketInfoHandler | MASTER-webpage ready")
		socket.on( 'sonarActivated', (obj) => {  //Obj is a JSON object
			console.log( "&&& WebpageServer sceneSocketInfoHandler | sonarActivated " );
			console.log(obj) 
			updateCallers( JSON.stringify(obj) )
		})
        socket.on( 'collision',     (obj) => {
		    var move =   moveMap.get(runningMovesIndex)
		    console.log( "WebpageServer sceneSocketInfoHandler  | collision detected " + obj
		            + " runningMovesIndex=" + runningMovesIndex
		            + " move="+ move
		            + " target=" + obj + " numOfSockets=" + Object.keys(sockets).length );
		    const info     = {  'collision' : move, 'target': obj}
		    moveMap.set(runningMovesIndex,"interrupted")
		    updateCallers( JSON.stringify(info) )
 		    answerToPost( JSON.stringify(info) );
 		} )
        socket.on('endmove', (moveIndex)  => {  //April2022
		    //console.log( "WebpageServer sceneSocketInfoHandler  | endmove  PRE index=" + moveIndex + " moveMap.size=" + moveMap.size);
      		var curMove     = moveMap.get(moveIndex)   //nome della mossa o interrupted
     		var answer       = { 'endmove' : true , 'move' : curMove }
            const answerJson = JSON.stringify(answer)
  		    if( curMove != "interrupted" ){
  		        updateCallers( answerJson )
   		    }
  		    answerToPost( answerJson );
   	        moveMap.delete(moveIndex)
  		    console.log( "WebpageServer sceneSocketInfoHandler  | endmove index=" + moveIndex + " moveMap.size=" + moveMap.size  );
 		    showMoveMap()
         })
       socket.on( 'disconnect',     () => {
        		delete sockets[key];
          		socketIndex--;
			    alreadyConnected = ( socketIndex == 0 )
        		console.log("WebpageServer sceneSocketInfoHandler  | disconnect socketIndex="+socketIndex)
        })
    })
}//sceneSocketInfoHandler


function answerToPost(  answerJson ){
   if( postResult != null ){
        console.log('WebpageServer | answerToPost  answer= ' + answerJson  );
        postResult.writeHead(200, { 'Content-Type': 'text/json' });
        postResult.statusCode=200
        postResult.write( answerJson  );
        postResult.end();
        postResult = null;
   }
}


function showMoveMap(){
    moveMap.forEach( (key,v) => console.log("WARNING: movemap key=" + key + " v="+v) )
}

function startServer() {
    console.log("WebpageServer  | startServer" )
    sceneSocketInfoHandler()
    initWs()
    hhtpSrv.listen(serverPort)
}
//MAIN
startServer()


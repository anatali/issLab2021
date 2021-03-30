/*
webguiServer.js
*/
const http              = require('http')
let express             = require('express');
let path                = require('path');
let fs                  = require('fs');
let bodyParser          = require('body-parser');

const WebSocketClient   = require('websocket').client   //interaction with 8091
const WebSocket         = require('ws');

//const request  = require('request') //deprecated

const { forward, connectAndSend, postTo8090 } = require('./webguiServerutils')
const app        = express();
const server     = http.createServer(app);  //initialize a simple http server

var   moveTime   = 600
var   turnTime   = 300
var   history    = "";

// view engine setup;
app.set('views', path.join(__dirname, './', 'views'));
app.set('view engine', 'ejs');

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.get('/', function (req, res) {
	console.log("webguiServer get " + __dirname );
    res.sendFile(path.join(__dirname, "index.html"));
  });

app.get('/guisimple', function (req, res) {
	console.log("webguiServer guisimple get " + __dirname );
	res.render("indexSimple.ejs", {})
  });

app.post('/guisimple', function (req, res) { res.render("indexSimple.ejs", {})  });

app.get('/guiJquery', function (req, res) {
	console.log("webguiServer guiJquery get " + __dirname );
	res.render("indexJquery.ejs", {})
  });
app.post('/guiJquery', function (req, res) { res.render("indexJquery.ejs", {})    });

app.get('/guisock', function (req, res) {
	console.log("webguiServer guisock get " + __dirname );
	res.render("indexSock.ejs", {})
  });
app.post('/guisock', function (req, res) { res.render("indexSock.ejs", {})  });


app.get('/info', function (req, res) { res.send( history ); });

app.get('/picture', function (req, res) {
  let img = fs.readFileSync(path.join(__dirname, "images/profile-1.jpg"));
  res.writeHead(200, {'Content-Type': 'image/jpg' });
  res.end(img, 'binary');
});

/*
THE CODE IN THIS DIR CAN be used in index.html
*/
app.use(express.static(path.join(__dirname, './jscode')));

//HANDLE POST from 'conventional' HTML GUI
app.post("/w", function(req, res,next)  {
    req.gui="guisimple"; postTo8090(wenvHost, "moveForward",addToHistory,moveTime ); next(); });
app.post("/s", function(req, res,next)  {
    req.gui="guisimple"; postTo8090(wenvHost, "moveBackward",addToHistory, moveTime); next(); });
app.post("/r", function(req, res,next)  {
    req.gui="guisimple"; postTo8090(wenvHost, "turnRight",addToHistory, turnTime);    next(); });
app.post("/l", function(req, res,next)  {
    req.gui="guisimple"; postTo8090(wenvHost, "turnLeft",addToHistory, turnTime);     next(); });
app.post("/h", function(req, res,next)  {
    req.gui="guisimple"; postTo8090(wenvHost, "alarm",addToHistory, 0);        next(); });


//HANDLE  utility commands
app.post("/conns", function(req, res,next)         { connectionHistory(); next()  });
app.post("/clearHistory", function(req, res,next)  { clearDisplayArea(); next()   });
//app.post("/duration", function(req, res,next)      { console.log("DDDD " ); moveTime=600; next()  });

//HANDLE POST from HTML GUI to send a POST to Wenv 8090
app.post("/l8090", function(req, res,next)  {
    req.gui="guiJquery"; postTo8090(wenvHost,'turnLeft',addToHistory, turnTime);    next()  });
app.post("/r8090", function(req, res,next)  {
    req.gui="guiJquery"; postTo8090(wenvHost, 'turnRight',addToHistory, turnTime);   next()  });
app.post("/w8090", function(req, res,next)  {
    req.gui="guiJquery"; postTo8090(wenvHost, 'moveForward',addToHistory, moveTime); next()  });
app.post("/s8090", function(req, res,next)  {
    req.gui="guiJquery"; postTo8090(wenvHost, 'moveBackward',addToHistory, moveTime);next()  });
app.post("/h8090", function(req, res,next)  {
    req.gui="guiJquery"; postTo8090(wenvHost, 'alarm',addToHistory,0);       next()  });

/*
* ====================== REPRESENTATION ================
*/
app.use( function(req,res){
	console.log("webguiServer | SENDING THE ANSWER " + res  + " json:" + req.accepts('json') )
	try{
      // if (req.accepts('json')) { res.send(history);		//give answer to curl / postman } else
	  //return res.render('index' );  //NO: we loose the message sent via socket.io
	  console.log( "webguiServer | req.gui=" + req.gui )
	  if( req.gui=="guisimple" )       res.render("indexSimple.ejs", {})
	  else if( req.gui=="guiJquery" )  res.render("indexJquery.ejs", {})
      else if( req.gui=="guisock" )    res.render("indexSock.ejs", {})
      else res.sendFile(path.join(__dirname, "index.html"))
	}catch(e){
	    console.log("webguiServer | SORRY ..." + e);}
	}
);
/*
 * ============ ERROR HANDLING =======
 */
 // catch 404 and forward to error handler;
 app.use(function(req, res, next) {
   var err = new Error('Not Found');
   err.status = 404;
   next(err);
 });

 // error handler;
 app.use(function(err, req, res, next) {
   // set locals, only providing error in development
   res.locals.message = err.message;
   res.locals.error = req.app.get('env') === 'development' ? err : {};

   // render the error page;
   res.status(err.status || 500);
   res.render('error');
 });


/*
function handlePostMove( cmd, msg, req, res, next ){
    console.log( "webguiServer |  handlePostMove in webguiServer.js "  + cmd )
    forward(cmd, "localhost");  //via TCP: NO MORE
    updateRobotState(  cmd );
    //res.sendFile(path.join(__dirname, "index.html"));  //
    //res.sendStatus(200);
    next();     //see REPRESENTATION
}
*/

/*
============================================================================================
WebSockets SECTION
require('ws');
receives commands and sends updates
============================================================================================
See https://www.ably.io/topic/websockets
https://medium.com/hackernoon/implementing-a-websocket-webguiServer-with-node-js-d9b78ec5ffa8
See https://www.html.it/pag/54040/websocket-webguiServer-con-node-js/
*/
var clients = 0;

const wsServer  = new WebSocket.Server({ server });
//wsServer.on('open', socket => { console.log("webguiServer | socket open ") });


wsServer.on('connection', (ws) => {
    console.log("webguiServer | client connected using ws ")
    displayHistory()
/*
HANDLE messages sent from the browser
*/
    ws.on('message', message => {
        //console.log("webguiServer | socket-on received : "+message)
        if( message=="turnRight" || message=="turnLeft" || message=="alarm" || message=="moveForward" || message=="moveBackward" ){
            postTo8090(wenvHost, message, addToHistory, moveTime);
        }else if( message.includes("duration-") ){
            moveTime = message.replace("duration-", "")
            console.log("webguiServer | socket-on - duration=" + moveTime);
        }
        else if( message.includes("close")) {
            console.log("webguiServer | socket-on - the client is disconnected ");
        }
  });
});

/*
     ws.on('close', function () {
        clients--;
        alert("client disconnected");
          wsServer.clients.forEach(client => {
              client.send(  "disconnection_connected=" + clients );
          });
                //io.sockets.emit('broadcast',{ description: clients + ' clients connected!'});
     });
*/
//-------------------------------------- WebSockets SECTION END

function clearDisplayArea(){
    history = ""
    displayHistory()  //done by
}
/*
function updateRobotState(message){
    history = history + "<br/>" + message;
    console.log(history);
    wsServer.clients.forEach(client => {
        client.send( history );
    });
}
*/
function displayHistory(){
   wsServer.clients.forEach(client => {
        client.send(   history )
   })
    //socket.emit('broadcast',{ description: clients + ' clients connected!'});
}

function addToHistory( msg ){
   history     = history + "<br/>" + msg
   displayHistory()
}

function connectionHistory(){
    addToHistory( history + "<br/>" + "connections=" + wsServer.clients.size )
}

/*
============================================================================================
ws8091 SECTION
require('websocket')
Is it useful to receive state data, in order to update the GUI and define some business logic
============================================================================================
*/
var client = new WebSocketClient();
    client.on('connectFailed', function(error) {
        console.log('WebSocketClient | connectFailed: ' + error.toString());
                if( wenvHost == "wenv" ) {
                    wenvHost = "localhost"      //change needed since required by postTo8090
                    client.connect( "ws://"+wenvHost+":8091" , '')
                }
    });

    client.on('connect', function(connection) {
        console.log('WebSocketClient | Connected')

        connection.on('error', function(error) {
            console.log("WebSocketClient | Error: " + error.toString());
        });
        connection.on('close', function() {
            console.log('WebSocketClient | Connection Closed');
        });
        connection.on('message', function(message) {
            console.log("WebSocketClient | Received  : " + message  )
             // { type: 'utf8', utf8Data: '{"endmove":true,"move":"turnRight"}' }
            if (message.type === 'utf8') {
                const msg = message.utf8Data
                console.log("WebSocketClient | Received: " + msg  )
                const msgJson = JSON.parse( msg ) //{"endmove":true,"move":"turnRight"}
                /*
                if(msgJson.endmove)   console.log("WebSocketClient | Received: endmove=" + msgJson.endmove)
                if(msgJson.collision) console.log("WebSocketClient | Received: collision=" + msgJson.collision)
                if(msgJson.sonarName)
     console.log("WebSocketClient | Received: sonar=" + msgJson.sonarName + " distance=" + msgJson.distance)
                */
                addToHistory( JSON.stringify( msgJson ) )
            }else{
                console.log("WebSocketClient | Received NO utf8: "   )
            }
    });
});

var wenvHost = "wenv" //"wenv" //"localhost"
const url      = "ws://"+wenvHost+":8091"
client.connect( url , '') //'echo-protocol'
//client.connect('ws://wenv:8091', '');
/*
try{
    client.connect('ws://localhost:8091', ''); //'echo-protocol'
}catch(e){
    client.connect('ws://wenv:8091', '');
}
*/
//-------------------------------------- ws8091 SECTION END



server.listen(3000, function () {
    //Template literals are enclosed by the back-tick (` `) (grave accent)
  console.log(`server listening on port ${server.address().port} with __dirname=${__dirname}`  );
});
/*
webguiserverUtils.js
*/

const axios    = require('axios')
//const net      = require('net')       //for TCP (obsolete)

//var host    = "localhost"; //"wenv";
var counter = 0;

/*
POST HTTP a Wenv
*/
async function postTo8090(host, move, addToHistory, duration ){
const URL = "http://"+ host + ":8090/api/move" ;
if( duration == null ) duration = 800
await axios({
            url: URL,
            data: { robotmove: move, time:duration },   //JSON msg
            method: 'POST',
            timeout: 1000,
            headers: { 'Content-Type': 'application/json' }
/*
  .post(URL, {
    robotmove: move
*/
    }).then(response => {
    console.log("serverutils postTo8090 | statusCode: " + response.status  )
    //console.log("serverutils postTo8090 | statusText: " + response.statusText);
    const answer = response.data
    //console.log( "serverutils postTo8090 | answer" + answer)  //"endmove": "false", "move": "turnLeft" }
    const collision = ! answer.endmove
    //addToHistory is given as argument
    if( response.status == "200")  addToHistory( "postTo8090:" + move + " | collision=" + collision )
    else addToHistory( move + " | response.status=" + response.status )
 })
  .catch(error => {
    console.error("postTo8090 error:" + error)
  })
}



/*
Invia su TCP (obsoleto da Jan 2021)

const sep      = ";"
function connectAndSend( msg  ){
    var client = new net.Socket();
    client.connect(8999, host, () => {
          // 'connect' listener
          console.log('serverUtils | connected to virtual robot server on ' + host + " counter=" + ++counter );
          client.write(msg+'\r\n');
          //client.end();
    })

    client.on('error', () => {
      console.log('serverUtils | ERROR with host=' + host);
      //if( host="localhost" ) forward( cmd, "wenv")  //in the case of docker-compose
    });
    client.on('data', (data) => {
      var v = data.toString();
      console.log("serverUtils | receives from wenv server: "+ v);
      //addToHistory(v);
    });
    client.on('end', () => {
      console.log('serverUtils | disconnected from server counter=' + counter );
    });
}

function forward( cmd  ){
    payload    =  "{ \"type\": \"" + cmd + "\", \"arg\": 800 }";
    msg        = sep+payload+sep;
    console.log('serverUtils | forward ' + msg ); //+ " client=" + client
    connectAndSend(msg);
}//forward
*/

exports.postTo8090   = postTo8090;
//module.exports = { forward, connectAndSend, postTo8090 }
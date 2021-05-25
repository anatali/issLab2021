/*
* =====================================
* frontend/uniboSupports/mqttUtils.js
* =====================================
*/
const mqtt   = require ('mqtt');  //npm install --save mqtt
const topic  = "unibo/qak/events";

var mqttAddr = 'mqtt://localhost'
//var mqttAddr = 'mqtt://192.168.43.229'
//var mqttAddr = 'mqtt://iot.eclipse.org'

var client   = mqtt.connect(mqttAddr);
var io  ; 	//Upgrade for socketIo;
var robotModel    = "none";
var sonarModel    = "none";
var roomMapModel  = "none";

//console.log("mqtt | client= " + client );

exports.setIoSocket = function ( iosock ) {  //called by applCode
 	io    = iosock;
	console.log("mqtt | SETIOSOCKET (to update the page) "  );
}


client.on('connect', function () {
	  client.subscribe( topic );
	  console.log("mqtt | client has connected successfully with " + mqttAddr);
});

contentEvent = function( evId, msgStr, endStr ){
  //msg(evId,event,SEMDER,none,evId(...),N)
  var start = msgStr.indexOf(evId+"(");
  if( start < 0 ) return "";
  var body    =  msgStr.substr(start);	
  var content =  body.substr( 0,body.indexOf(endStr)+endStr.length );	
  console.log("mqtt | contentEvent "+ evId + ":" +  content);
  return content;
}

checkEvent  = function( evId, msgStr ){
  //msg(obstacle,event,filter,none,obstacle(6),503)
  //msg(polar,event,forradar,none,polar(6,0),504)
  var ev =  contentEvent( evId, msgStr, ")" );
  if( ev.length > 0 ) io.sockets.send( ev );
 }
 
checRobotStatekEvent = function( msgStr ){
  //msg(modelContent,event,resourcemodel,none,content(robot(state( pos(X,Y),dir(D) ))),74) 
  var ev = contentEvent( "content", msgStr, ")))" );
  //ev=content(robot(state(pos(0,0),dir(SUD)))
  if( ev.length > 0 ) {
  	var a = ev.indexOf("state(")
  	var b = ev.indexOf(")))")-3
  	var outS = ev.substr( a, b );
  	console.log("mqtt | outS "+ outS + " " + a + " " + b  );
  	io.sockets.send( outS );
  }
}
//The message usually arrives as buffer, so I had to convert it to string data type;
client.on('message', function (topic, message){
  //console.log("mqtt io="+ io );
  var msgStr = message.toString(); //message comes as buffer
  //console.log("mqtt | RECEIVES:"+ msgStr );    
  //checkEvent( "obstacle", msgStr );
  //checkEvent( "polar",    msgStr ); 
  //checRobotStatekEvent( msgStr );
});
 
exports.publish = function( msg, topic ){
	//console.log('"mqtt |  publish ' + client);
	client.publish(topic, msg);
}

exports.getrobotmodel = function(){
	return robotModel;
}
exports.getsonarrobotmodel = function(){
	return sonarModel;
}
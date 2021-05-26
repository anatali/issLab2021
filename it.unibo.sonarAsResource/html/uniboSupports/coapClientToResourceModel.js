/*
uniboSupports/coapClientToResourceModel
*/
const coap             = require("node-coap-client").CoapClient; 
var coapAddr           = "coap://192.168.1.45:8028"        
var coapResourceAddr   = coapAddr + "/ctxsonarresource/sonarresource"
/*
coap
    .tryToConnect( coapAddr )
    .then((result ) => { //  true or error code or Error instance  
        console.log("coap | connection done"); // do something with the result  
    })
    ;
*/
/* -------------------------------------------
function handeData( response ){
	console.log("		coapClientToResourceModel | handeData " + response.payload);
}
*/

function createCoapClient( resourceAddr ){
console.log("		coapClientToResourceModel | createCoapClient "  );
coap
    .observe(
        resourceAddr /* string */,
        "get" /* "get" | "post" | "put" | "delete" */,
        handle.handeData //handeData /* function */
        //[payload /* Buffer */,]
        //[options /* RequestOptions */]
    )
    .then(() => { console.log("		coapClientToResourceModel | observe setup " ); /* observing was successfully set up */})
    .catch(err => { console.log("		coapClientToResourceModel | observe error " + err )  /* handle error */ })
    ;
}

 
exports.setcoapAddr = function ( addr ){
	//coapAddr = "coap://"+ addr + ":5683";
	//coapResourceAddr   = coapAddr + "/robot/pos" // coap://localhost:5683/robot/pos
	coapResourceAddr = addr
	console.log("coap | coapResourceAddr=" + coapResourceAddr);
	createCoapClient( coapResourceAddr   );
	createCoapClient( sensorResourceAddr );
}

exports.coapGet = function (  ){
	coap
	    .request(
	         coapResourceAddr,
	        "get" /* "get" | "post" | "put" | "delete" */
 	        //[payload /* Buffer */,
	        //[options /* RequestOptions */]]
	    )
	    .then(response => { 			/* handle response */
	    	console.log("coap get done> " + response.payload );}
	     )
	    .catch(err => { /* handle error */ 
	    	console.log("coap get error> " + err );}
	    )
	    ;
	    
}//coapPut

exports.coapPut = function (  cmd ){ 
console.log("PUT " + coapResourceAddr);
	coap
	    .request(
	        coapResourceAddr,     
	        "put" ,			                          // "get" | "post" | "put" | "delete"   
	        new Buffer(cmd )                          // payload Buffer 
 	        //[options]]							//  RequestOptions 
	    )
	    .then(response => { 			// handle response  
	    	console.log("coap put done> " + cmd + " " + response);} 
	     )
	    .catch(err => { // handle error  
	    	console.log("coap | put error> " + err + " for cmd=" + cmd);}
	    )
	    ;
	    
}//coapPut

const myself   = require('./coapClientToResourceModel');

function test(){
 	//console.log("GET");
  	myself.coapGet();
 	//console.log("PUT");
 	myself.coapPut("r")
 	myself.coapGet();
}

//test()
 

/*
 * ========= EXPORTS =======
 */

//module.exports = coap;

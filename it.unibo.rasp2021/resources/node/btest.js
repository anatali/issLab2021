//const readline        = require('../../../it.unibo.qak20.sensors/resources/node_modules/readline-sync');
const coap            = require("../../../it.unibo.qak20.sensors/resources/node_modules/node-coap-client").CoapClient; 
//var url               = require('url');
var coapResourceAddr  = "coap://192.168.1.175:8037/ctxdcreceiver/dcreceiver";

//var qurl = url.parse(coapResourceAddr, true);
//var q = url.format(qurl)
//var q = new url( coapResourceAddr );


console.log( "CREATED connection   " + coapResourceAddr  ) 

createCoapObserver = function( ){
console.log("createCoapObserver "  );
coap.observe(
        coapResourceAddr,
        "get",
        handeData
     )
    .then( () =>  { console.log("observing ...  " ); })
    .catch(err => { console.log("observing error " + err );  });
}//createCoapObserver

handeData = function( response ){
    console.log("observed: " + response.payload);
}//handeData

coapGet = function ( ){
    console.log("coapGet " + coapResourceAddr);
    coap.request(
             coapResourceAddr,
             "get" )
    .then(response => { console.log("coap get done> " + response.payload );}  )
    .catch(err =>     { console.log("coapGet error> " + err );}                  )
    ;   
}

coapPut = function ( cmd ){ 
console.log("PUT " + coapResourceAddr);
coap.request(
    coapResourceAddr,     
    "put" ,  
    new Buffer(cmd )         // payload Buffer 
    //[options]]             //  RequestOptions 
    )
    .then(response => { console.log("coap put done> " + cmd + " " + response);} )
    .catch(err => { console.log("coap | put error> " + err + " for cmd=" + cmd);}
    )
    ;   
}//coapPut

forwardMsg = function( move ){
    msg = "msg(cmd,dispatch,py,basicrobot,cmd("+move+"),1)";
    console.log("forwarqdMsg  forwardMsg= :" , msg  );
    coapPut(msg);
}//forwardMsg

// true sync solution
const delay = (ms) => require("child_process")
    .execSync(`"${process.argv[0]}" -e setTimeout(function(){},${ms})`);

coapGet()  
createCoapObserver()
//forwardMsg( "s" )  
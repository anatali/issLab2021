  const readline  = require('../../../it.unibo.qak20.sensors/resources/node_modules/readline-sync');
  var coap        = require('../../../it.unibo.qak20.sensors/resources/node_modules/coap')
   
  var server      = coap.createServer({ type: 'udp6' })
  
  server.on('request', function(req, res) {
    console.log("req.url="+req.url)
  	res.end('Hello ' + req.url.split('/')[1] + '\n')
  })
  
    
  server.listen(  function() {
  var req = coap.request('coap://192.168.1.22:8037/ctxdcreceiver/dcreceiver', {observe:true} )
  req.on('response', function(res) {
    res.pipe(process.stdout)
    res.on('end', function() {
    	console.log("BYE " + res)
        //process.exit(0)
    })
  })
  req.end()
  })

console.log("server created " + server)
/*
const readlineSync   = require('../../../it.unibo.qak20.sensors/resources/node_modules/readline-sync');
console.log("readlineSync ok")
 


var req1 = coap.request('coap://192.168.1.22:8037/ctxdcreceiver/dcreceiver', {observe:true} )
  
  req1.on('response', function(res) {
    res.pipe( process.stdout )
  })
 
  req1.end()


var sleep = require('sleep'); 
console.log("start");
sleep.sleep(5);
console.log("end");


const sleep2 = (ms) => require("child_process")
    .execSync(`"${process.argv[0]}" -e setTimeout(function(){},${ms})`);
console.log("start sleep");
sleep2(30000)
console.log("end sleep");
  
//var userName = readlineSync .question('Who are you?' );

console.log("done")
*/
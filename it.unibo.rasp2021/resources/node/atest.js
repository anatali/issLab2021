  const readline  = require('../../../it.unibo.qak20.sensors/resources/node_modules/readline-sync');
  var coap        = require('../../../it.unibo.qak20.sensors/resources/node_modules/coap')
   
 coap.createServer(function(req, res) {
  res.end('Hello ' + req.url.split('/')[1] + '\n')
 }).listen(function() {
  console.log("listen")
  
  var req = coap.request('coap://192.168.1.22:8037/ctxdcreceiver/dcreceiver' , {observe:true})  //coap://localhost/Matteo 

  req.on('response', function(res) {
    console.log("res="+res)
    res.pipe(process.stdout)
//    res.on('end', function() {
//       console.log("END")
      //process.exit(0)
//     })
  })
  
  req.end()

 
 
  
 
})  
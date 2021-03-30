/*
jscode/jqutils.js
*/

$(document).ready(function(){
  $("p").click(function(){ $(this).hide(); });

//USED BY index (console selection)
$( "#guisimple" ).click(function() {  loadGui( "guisimple") });
$( "#guiJquery" ).click(function() {  loadGui( "guiJquery") });
$( "#guisock" ).click(function()   {  loadGui( "guisock") });

//MOVETIME
$( "#duration" ).dblclick(function() {  requestTodoTheMove("duration-"+$(this).val())  }); //console.log( $(this).val() )

//USED BY SOCKET.IO-BASED GUI (guisock) - requestTodoTheMove defined in webSocketUtils
    $( "#rsocket" ).click(function() {  requestTodoTheMove("turnRight") });
    $( "#lsocket" ).click(function() {  requestTodoTheMove("turnLeft") });
    $( "#wsocket" ).click(function() {  requestTodoTheMove("moveForward") });
    $( "#ssocket" ).click(function() {  requestTodoTheMove("moveBackward") });
    $( "#hsocket" ).click(function() {  requestTodoTheMove("alarm") });

});

//USED BY POST with jQuery  (guiJquery)
$(function () {
/*  USED WITH TCPServer (version before Jan 2021)
    $( "#wjquery" ).click(function() { sendRequestData( "w") });     //defined here
    $( "#sjquery" ).click(function() { sendRequestData( "s") });
    $( "#rjquery" ).click(function() { sendRequestData( "r") });
    $( "#ljquery" ).click(function() { sendRequestData( "l") });
    $( "#hjquery" ).click(function() { sendRequestData( "h") });
*/
    $( "#lpost8090").click(function() { doPostToWenv( "l8090") })   //defined here
    $( "#rpost8090").click(function() { doPostToWenv( "r8090") })
    $( "#wpost8090").click(function() { doPostToWenv( "w8090") })
    $( "#spost8090").click(function() { doPostToWenv( "s8090") })
    $( "#hpost8090").click(function() { doPostToWenv( "h8090") })

//USED BY the human user
    $( "#displayconns" ).click(function() {  doPostToWenv( "conns") });  //defined here
    $( "#clear" ).click(function() {  doPostToWenv( "clearHistory") });

});

/*
======================================================================
*/

function loadGui(gui, method){
     var myip = location.host;
     var url = "http://"+myip+"/"+gui
     doPost(gui, method, url);
}

function doPostToWenv( params, method ) {
     var myip = location.host;
     var url = "http://"+myip+"/"+params
     doPost(params, method, url);
}

function doPost( params, method, url ) {
    console.log("doPostToWenv in jscode/jqutils " + params);
    method = method || "post"; // il metodo POST usato di default
    //console.log(" doPostToWenv  params=" + params + " method=" + method);
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", url);
    var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", "move");
        hiddenField.setAttribute("value", params);
     	//console.log(" doPostToWenv " + hiddenField.getAttribute("name") + " " + hiddenField.getAttribute("value"));
        form.appendChild(hiddenField);
    document.body.appendChild(form);
    console.log("body children num= "+document.body.children.length );
    form.submit();
    document.body.removeChild(form);
    console.log("body children num= "+document.body.children.length );
}


/*
function getGui(gui){
fetch("http://"+location.host+"/"+gui)
  .then(function (response) {
    console.log( response )
    //$(document).load( response )  //???
    return response ;
  })
  .then(function (myJson) {
    console.log(myJson );
  })
  .catch(function (error) {
    console.log("Error: " + error);
  });
}
*/
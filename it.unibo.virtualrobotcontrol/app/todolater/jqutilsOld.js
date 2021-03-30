$(document).ready(function(){
  $("p").click(function(){
    $(this).hide();
  });
});



$(function () {

/*
     $("form").on('submit', function (event) {
         console.log( event  )
         event.preventDefault();
         //$( "#rpage" ).click(function() {  sendRequestData("r") });
         //sendRequestData("rpage");
    });*/
    $( "#connect" ).click(function() { alert("connect"); connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });

    $( "#ww" ).click(function() { sendRequestData( "w") });
    $( "#ss" ).click(function() { sendRequestData( "s") });
    $( "#rr" ).click(function() { sendRequestData("r")  });
    $( "#ll" ).click(function() { sendRequestData( "l") });
    $( "#zz" ).click(function() { sendRequestData( "z") });
    $( "#xx" ).click(function() { sendRequestData( "x") });
    $( "#pp" ).click(function() { sendRequestData( "p") });
    $( "#hh" ).click(function() { sendRequestData( "h") });

//USED BY SOCKET.IO-BASED GUI
    $( "#rsocket" ).click(function() {  sendTheMove("r") });
    $( "#lsocket" ).click(function() {  sendTheMove("l") });

/*
    $( "#h" ).click(function() {  sendTheMove("h") });
    $( "#w" ).click(function() {  sendTheMove("w") });
    $( "#s" ).click(function() {  sendTheMove("s") });
    $( "#x" ).click(function() {  sendTheMove("x") });
    $( "#z" ).click(function() {  sendTheMove("z") });
    $( "#p" ).click(function() {  sendTheMove("p") });
*/
    //$( "#rr" ).click(function() { console.log("submit rr"); redirectPost("r") });
    //$( "#rrjo" ).click(function() { console.log("submit rr"); jqueryPost("r") });

    //$( "#r" ).click(function() {  alert("r") }); //{  sendRequestData("r") });
    //$( "#l" ).click(function() {  sendRequestData("l") });


//USED BY POST-BASED BOUNDARY
    $( "#start" ).click(function() { sendRequestData( "w") });
    $( "#stop" ).click(function()  { sendRequestData( "h") });

	$( "#update" ).click(function() { sendUpdateRequest(  ) });


});

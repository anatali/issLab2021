var stompClient = null;
var hostAddr = "http://localhost:8085/move"; //ex 50850
var oldResponse = null;
var idTable1 = null;
var idTable2 = null;
var kickResponse = null;
var oldKickResponse = null;

//alert("app0.js loading ...... ");


//SIMULA UNA FORM che invia comandi POST
function sendRequestData( params, method) {
    method = method || "post"; // il metodo POST � usato di default
    //alert	(" sendRequestData  params=" + params + " method=" + method + " hostAddr=" + hostAddr);
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", hostAddr);
    var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", "move");
        hiddenField.setAttribute("value", params);
     	//console.log(" sendRequestData " + hiddenField.getAttribute("name") + " " + hiddenField.getAttribute("value"));
        form.appendChild(hiddenField);
    document.body.appendChild(form);
    console.log("body children num= "+document.body.children.length );
    form.submit();
    document.body.removeChild(form);
    console.log("body children num= "+document.body.children.length );
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);

    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }

    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/it-unibo-iss');
    //alert("connect " + socket);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        stompClient.subscribe('/topic/display-tearoomstatemanager', function (msg) {
            updateStatemanager(JSON.parse(msg.body).content);
        });
        stompClient.subscribe('/topic/display-smartbell', function (msg) {
            updateClient(JSON.parse(msg.body).content);
        });
        stompClient.subscribe('/topic/display-maxstaytime', function (msg) {
            kickClient(JSON.parse(msg.body).content);
        });
    });
}
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendUpdateRequest() {
	console.log("sendUpdateRequest");
    stompClient.send("/app/update", {}, JSON.stringify({'name': 'update'}));
}

function replaceAll(str, f, s) {
    return str.split(f).join(s);
}

function sendTheMove(move) {
    console.log("sendTheMove " + move);
    stompClient.send("/app/move", {}, JSON.stringify({'name': move }));
}

//USED BY SOCKET.IO-BASED GUI
$( "#h" ).click(function() { sendTheMove("h") });
$( "#w" ).click(function() { sendTheMove("w") });
$( "#s" ).click(function() { sendTheMove("s") });
$( "#r" ).click(function() { sendTheMove("r") });
$( "#l" ).click(function() { sendTheMove("l") });
$( "#x" ).click(function() { sendTheMove("x") });
$( "#z" ).click(function() { sendTheMove("z") });


//alert("app0.js loaded ");
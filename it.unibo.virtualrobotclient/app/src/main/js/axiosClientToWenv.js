/*
axiosClientToWenv.js
===============================================================
walks along the boundary bysing callbacks, no global state
===============================================================
*/
const axios = require('axios')
const URL   = 'http://localhost:8090/api/move' ;


function ahead(numOfSteps){
     domove("moveForward", numOfSteps, goon, ko)
}

function goon(numOfSteps){
     domove("moveForward", numOfSteps, ahead, ko)
}

function ko(numOfSteps){
    //console.log("ko numOfSteps=" + numOfSteps  )
    if( numOfSteps++ < 4 )  domove("turnLeft", numOfSteps, goon, ko)
    else  domove("turnLeft", numOfSteps, terminate, terminate)  //just to return to initial state
}

function terminate(){
    console.log("Buoundary explored"   )
}

//------------------------------------------------------------
function domove(move, numOfSteps, callbackOk, callbackCollision)  {
    axios({
            url: URL,
            data: { robotmove: move, time: 600 },
            method: 'POST',
            timeout: 900,
            headers: { 'Content-Type': 'application/json' }
    }).then(response => {   //continues when the action has been done
        console.log("axiosClientToWenv domove | response.data: " )
        var answer = response.data
        console.log(  answer )
        collision =  ! answer.endmove
        console.log(  "collision=" + collision )
        if( collision ) callbackCollision(numOfSteps); else callbackOk(numOfSteps)
  })
  .catch(error => {
    console.error(error)
  })
}


//main
ahead(1)
/*
===============================================================
wsclientBoundaryLogic.js
used also in a webpage
===============================================================
*/
var  numOfSteps = 1
var  moves      = ""
var isInPage    = ( typeof location  !=  'undefined' )

    function cmdRobot(move, time, connection) {
        const moveJson = '{"robotmove":"'+ move +'"'+', "time":'+time+'}'
        console.log("cmdRobot moveJson:" + moveJson + " isInPage=" + isInPage + " conn=" + connection);
        //if( isInPage ) return
        if (connection ) { connection.send(moveJson) }
    }

function elabMoveResponse( crilJsonMsg  ){
        console.log( crilJsonMsg  )
        if(crilJsonMsg.collision) console.log("Received: collision=" + crilJsonMsg.collision)
        if(crilJsonMsg.sonarName){
            console.log('\u0007');  //RING THE BELL
            console.log("sonar=" + crilJsonMsg.sonarName + " distance=" + crilJsonMsg.distance)
        }
        if(crilJsonMsg.endmove){
            //crilMsg: {"endmove":"...","move":"..."}
            if( jobCounter++ < 1) doJob()
            //walkLogic.walkBoundary(crilJsonMsg, conn8091)
        }
}

function walkBoundary( crilJsonMsg, connection  ){
    console.log("wsclientBoundaryLogic | walkBoundary numOfSteps=" + numOfSteps + " p=" + moves)
    console.log( crilJsonMsg  )
        if(crilJsonMsg.collision){
            console.log("Received: collision=" + crilJsonMsg.collision)
            return
        }
        if(crilJsonMsg.sonarName){
            console.log('\u0007');  //RING THE BELL
            console.log("sonar=" + crilJsonMsg.sonarName + " distance=" + crilJsonMsg.distance)
            return
        }
    //endmove
    console.log( crilJsonMsg.move  )
    if( crilJsonMsg.move == "turnLeft"){
        if( numOfSteps < 4 ) {
             numOfSteps++
             moves = moves + "l"
             cmdRobot("moveForward", 400, connection)
        }else if( numOfSteps++ >= 4 ){
            moves = moves + "l"
            console.log("Boundary explored; moves=" + moves  )
        }
       return
    }
    //answer to moveForward
    if( crilJsonMsg.endmove == 'true' ){
        moves = moves + "w"
        cmdRobot("moveForward", 400, connection)
    }
    else cmdRobot("turnLeft", 300, connection)

}
 
//module.exports = { walkBoundary }
//module.exports = { cmdRobot     }
if ( typeof location ==  'undefined'){
    exports.walkBoundary = walkBoundary;
    exports.cmdRobot     = cmdRobot;
}


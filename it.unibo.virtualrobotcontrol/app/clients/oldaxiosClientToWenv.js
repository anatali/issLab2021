/*
axiosClientToWenvOld
*/
const axios = require('axios')
const URL   = 'http://localhost:8090/api/move' ;
var   lastCollision = false
const path = ['sud', 'east', 'north', 'west']

async function doTest(){
    await postTo8090("turnRight")
    console.log("axiosClientToWenv | lastCollision: " + lastCollision )
    await postTo8090("moveForward")
    console.log("axiosClientToWenv | lastCollision: " + lastCollision )
}

async function boundary(){
    for( const i in path ){
        while( !lastCollision ){
            await postTo8090("moveForward")
        }
        await postTo8090("turnLeft")
    }
}
/*
Using callbacks
*/
var numOfSteps = 1
async function walkAlongBoundary(){
    var v = await walkAhead()
    console.log("BYE walkAlongBoundary " + v)
}
async function walkAhead(){
    var v = await domove("moveForward", walkAhead, changeDirection )
    console.log("BYE walkAhead " + v )
}

async function changeDirection(){
    if( numOfSteps++ < path.length ){
        await domove("turnLeft", walkAhead, changeDirection )
        console.log("BYE changeDirection")
    }
}
 function resolveMove(move) {
    console.log("resolveMove PRE lastCollision=" + lastCollision )
    return new Promise( resolve => {
        setTimeout( () => {
            postTo8090("moveForward"  )
            console.log("resolveMove POST lastCollision=" + lastCollision )
            resolve(lastCollision + " " + move); }, 2000);
    })
}

async function ahead(){
    //console.log("ahead " +  numOfSteps  )
    await dopost8090("moveForward", ahead, ko)
}

async function ko(){
    if( numOfSteps++ < 4 ) await dopost8090("turnLeft", ahead, ko)
    else await dopost8090("turnLeft", terminate, terminate)
}

function terminate(){
    console.log("Buondary explored"   )
}
ahead()
//walkAlongBoundary()
//boundary()
//doTest()

async function dopost8090(move, callbackOk, callbackCollision)  {
    axios({
            url: URL,
            data: { robotmove: move },
            method: 'POST',
            timeout: 900,
            headers: { 'Content-Type': 'application/json' }
    }).then(response => {
        //console.log("axiosClientToWenv dopost8090 | data: " + response.data)
        const collision = JSON.parse( response.data ).collision
        console.log( "dopost8090 move=" + move + " numOfSteps=" + numOfSteps + " collision= " + collision )
        if( collision ) callbackCollision(); else callbackOk()
  })
  .catch(error => {
    console.error(error)
  })
}
/*
The async and await keywords enable asynchronous, promise-based behavior
to be written in a cleaner style, avoiding the need to explicitly configure promise chains.

Async functions can contain zero or more await expressions.
async function foo() { return 1 } is equivalent to:  function foo() { return Promise.resolve(1) }
*/
async function postTo8090(move)  {
lastCollision = false
await axios({
            url: URL,
            data: { robotmove: move },
            method: 'POST',
            timeout: 900,
            headers: { 'Content-Type': 'application/json' }
			//.post(URL, { robotmove: move })
    }).then(response => {
    //console.log("axiosClientToWenv postTo8090 | statusCode: " + response.status  )
    console.log("axiosClientToWenv postTo8090 | data: " + response.data)
    //console.log("axiosClientToWenv postTo8090 | statusText: " + response.statusText)
    const answer = JSON.parse( response.data )
    console.log("collision= " + answer.collision + " for:" + answer.move )
    //return answer.collision               //NO, since it is async!!!
    lastCollision = answer.collision        //MODIFY the environment
  })
  .catch(error => {
    console.error(error)
  })
}



async function domove(move, callbackOk, callbackCollision)  {
await axios({
            url: URL,
            data: { robotmove: move },
            method: 'POST',
            timeout: 900,
            headers: { 'Content-Type': 'application/json' }
			//.post(URL, { robotmove: move })
    }).then(response => {
        console.log("axiosClientToWenv domove | numOfSteps= " + numOfSteps + " data: " + response.data)
        const collision = JSON.parse( response.data ).collision
        //if( collision ) callbackCollision(); else callbackOk()
        return collision
  })
  .catch(error => {
    console.error(error)
  })

}



